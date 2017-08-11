package com.bw30.open.wft.common.translate.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.bw30.open.wft.common.translate.processor.model.XCell;
import com.bw30.open.wft.common.translate.processor.model.XRow;
import com.bw30.open.wft.common.translate.processor.util.StringUtil;

/**
 * Excel-2007行级处理器，ExcelRowProcessor的实现类
 * 
 * @author raymond
 * @version 1.0
 */
public abstract class Excel2007RowProcessor implements ExcelRowProcessor {

	enum xssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER
	}

	int countrows = 0;

	public static final String SHEET_ID_TAG = "rId";

	private MyXSSFSheetHandler hander;
	private OPCPackage pkg;
	private InputStream is; // 输入流
	private XSSFReader reader;
	private XMLReader parser;

	/**
	 * @param is
	 *            ioStream
	 * @throws Exception
	 */
	public Excel2007RowProcessor(InputStream is) throws Exception {
		this.is = is;
		this.init(is);
	}

	/**
	 * 构造Excel-2007行级解析器
	 * 
	 * @param filename
	 * @throws java.io.IOException
	 */
	public Excel2007RowProcessor(String filename) throws Exception {
		if (filename.endsWith(".xls")) {
			throw new Exception("Excel板式与解析器不匹配，解析器仅支持Excel-2007及以上版本。");
		}
		this.is = new FileInputStream(new File(filename));
		this.init(is);
	}

	private void init(InputStream is) throws Exception {
		this.pkg = OPCPackage.open(is);
		ReadOnlySharedStringsTable sharedStringsTable = new ReadOnlySharedStringsTable(
				pkg);
		this.reader = new XSSFReader(pkg);
		StylesTable styles = reader.getStylesTable();
		this.hander = new MyXSSFSheetHandler(styles, sharedStringsTable);
		this.parser = fetchSheetParser();
	}

	/**
	 * 辅助实现方法，xml解析
	 * 
	 * @param sst
	 * @return
	 * @throws org.xml.sax.SAXException
	 * @throws ParserConfigurationException
	 */
	private XMLReader fetchSheetParser() throws SAXException,
			ParserConfigurationException {
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader parser = saxParser.getXMLReader();
		parser.setContentHandler(hander);
		return parser;
	}

	/**
	 * 处理所有sheet
	 */
	public void processByRow() throws Exception {
		Iterator<InputStream> sheets = reader.getSheetsData();
		while (sheets.hasNext()) {
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
		}
	}

	/**
	 * 处理指定索引的sheet
	 */
	public void processByRow(int optSheetIndex) throws Exception {

		// rId2 found by processing the Workbook
		// 根据 rId# 或 rSheet# 查找sheet
		InputStream sheet = reader.getSheet(SHEET_ID_TAG + optSheetIndex);
		InputSource sheetSource = new InputSource(sheet);
		parser.parse(sheetSource);
		sheet.close();
	}

	public void stop() throws IOException {
		if (pkg != null) {
			pkg.close();
		}
	}

	/**
	 * 处理行数据的策略
	 */
	public abstract void processRow(XRow row);

	/**
	 * 辅助实现类，解析excel元素的句柄
	 * 
	 * @author raymond
	 * @version 1.0
	 */
	public class MyXSSFSheetHandler extends DefaultHandler {
		// // Table with styles
		private StylesTable stylesTable;
		// Table with unique strings
		private ReadOnlySharedStringsTable sharedStringsTable;
		// Set when V start element is seen
		private boolean vIsOpen;
		// used when cell close element is seen.
		private xssfDataType nextDataType;

		// Used to format numeric cell values.
		private short formatIndex;
		private String formatString;
		private final DataFormatter formatter;

		private int thisColumn = -1;
		// The last column printed to the output stream
		private int lastColumnNumber = -1;

		private List<String> rowList = new ArrayList<String>();

		// Gathers characters as they are seen.
		private StringBuffer value;

		public MyXSSFSheetHandler(StylesTable styles,
				ReadOnlySharedStringsTable strings) {
			this.stylesTable = styles;
			this.sharedStringsTable = strings;
			this.value = new StringBuffer();
			this.nextDataType = xssfDataType.NUMBER;
			this.formatter = new DataFormatter();
		}

		// @see
		// org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		// java.lang.String, java.lang.String, org.xml.sax.Attributes)
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {

			if ("inlineStr".equals(name) || "v".equals(name)) {
				vIsOpen = true;
				// Clear contents cache
				value.setLength(0);
			}
			// c => cell
			else if ("c".equals(name)) {
				// Get the cell reference
				String r = attributes.getValue("r");
				int firstDigit = -1;
				for (int c = 0; c < r.length(); ++c) {
					if (Character.isDigit(r.charAt(c))) {
						firstDigit = c;
						break;
					}
				}
				thisColumn = nameToColumn(r.substring(0, firstDigit));

				// Set up defaults.
				this.nextDataType = xssfDataType.NUMBER;
				this.formatIndex = -1;
				this.formatString = null;
				String cellType = attributes.getValue("t");
				String cellStyleStr = attributes.getValue("s");
				if ("b".equals(cellType))
					nextDataType = xssfDataType.BOOL;
				else if ("e".equals(cellType))
					nextDataType = xssfDataType.ERROR;
				else if ("inlineStr".equals(cellType))
					nextDataType = xssfDataType.INLINESTR;
				else if ("s".equals(cellType))
					nextDataType = xssfDataType.SSTINDEX;
				else if ("str".equals(cellType))
					nextDataType = xssfDataType.FORMULA;
				else if (cellStyleStr != null) {
					// It's a number, but almost certainly one
					// with a special style or format
					int styleIndex = Integer.parseInt(cellStyleStr);
					XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
					this.formatIndex = style.getDataFormat();
					this.formatString = style.getDataFormatString();
					if (this.formatString == null)
						this.formatString = BuiltinFormats
								.getBuiltinFormat(this.formatIndex);
				}
			}

		}

		// @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
		// java.lang.String, java.lang.String)
		public void endElement(String uri, String localName, String name)
				throws SAXException {

			String thisStr = null;

			// v => contents of a cell
			if ("v".equals(name)) {
				// Process the value contents as required.
				// Do now, as characters() may be called more than once
				switch (nextDataType) {
				case BOOL:
					char first = value.charAt(0);
					thisStr = first == '0' ? "FALSE" : "TRUE";
					break;

				case ERROR:
					thisStr = "\"ERROR:" + value.toString() + '"';
					break;

				case FORMULA:
					// A formula could result in a string value,
					// so always add double-quote characters.
					thisStr = '"' + value.toString() + '"';
					break;

				case INLINESTR:
					// TODO: have seen an example of this, so it's untested.
					XSSFRichTextString rtsi = new XSSFRichTextString(
							value.toString());
					thisStr = '"' + rtsi.toString() + '"';
					break;

				case SSTINDEX:
					String sstIndex = value.toString();
					try {
						int idx = Integer.parseInt(sstIndex);
						XSSFRichTextString rtss = new XSSFRichTextString(
								sharedStringsTable.getEntryAt(idx));
						thisStr =  rtss.toString();
					} catch (NumberFormatException ex) {
						ex.printStackTrace();
					}
					break;

				case NUMBER:
					String n = value.toString();
					if (this.formatString != null)
						thisStr = formatter.formatRawCellContents(
								Double.parseDouble(n), this.formatIndex,
								this.formatString);
					else
						thisStr = n;
					break;

				default:
					thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
					break;
				}

				// Output after we've seen the string contents
				// Emit commas for any fields that were missing on this row
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
				// Might be the empty string.
				rowList.add(thisColumn, thisStr);
				// Update column
				if (thisColumn > -1) {
					lastColumnNumber = thisColumn;
				}

			} else if ("row".equals(name)) {
				// Print out any missing commas if needed
				XRow row = new XRow();
				for (int i = 0; i < rowList.size(); i++) {
					XCell cell = new XCell();
					cell.setColumnIndex(i + 'A');
					cell.setRowIndex(countrows + 1);
					cell.setValue((String) rowList.get(i));
					row.setRowIndex(countrows + 1);
					row.addCell(cell);
				}
				// We're onto a new row
				if (!isBlankRow(row)) {
					processRow(row);
				}
				rowList.clear(); // empty the rowList;
				countrows++;
				lastColumnNumber = -1;
			}

		}

		// Captures characters only if a suitable element is open.
		// Originally was just "v"; extended for inlineStr also.
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (vIsOpen)
				value.append(ch, start, length);
		}

		// Converts an Excel column name like "C" to a zero-based index.
		// @param name
		// @return Index corresponding to the specified name
		private int nameToColumn(String name) {
			int column = -1;
			for (int i = 0; i < name.length(); ++i) {
				int c = name.charAt(i);
				column = (column + 1) * 26 + c - 'A';
			}
			return column;
		}

		private boolean isBlankRow(XRow row) {
			boolean b = true;
			for (int i = 0; i < row.getCellsSize(); i++) {
				XCell cell = row.getCell(i);
				if (StringUtil.hasValue(cell.getValue())) {
					b = false;
					break;
				}
			}
			return b;
		}
	}
}
