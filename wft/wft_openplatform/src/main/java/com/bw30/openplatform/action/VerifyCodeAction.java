package com.bw30.openplatform.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

public class VerifyCodeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3534338252270733157L;
	/**
	 * 验证码图片的宽度。
	 */
	private int width = 80;

	/**
	 * 验证码图片的高度。
	 */
	private int height = 30;

	/**
	 * 验证码字符个数
	 */
	private int codeCount = 4;

	/**
	 * 字体高度
	 */
	private int fontHeight;

	/**
	 * 第一个字符的x轴值，因为后面的字符坐标依次递增，所以它们的x轴值是codeX的倍数
	 */
	private int codeX;

	/**
	 * codeY ,验证字符的y轴值，因为并行所以值一样
	 */
	private int codeY;

	/**
	 * codeSequence 表示字符允许出现的序列值
	 */
	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
			'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'K', 'L', 'M',
			'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'x', 'y', 'z', '2',
			'3', '4', '5', '6', '7', '8', '9' };

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public String execute() {
		codeX = (width - 6) / (codeCount + 1);
		fontHeight = height - 10;
		codeY = height - 7;
		// 定义图像buffer
		BufferedImage buffer = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D grp = buffer.createGraphics();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		grp.setColor(Color.LIGHT_GRAY);
		grp.fillRect(0, 0, width, height);
		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Arial", Font.PLAIN, fontHeight);
		// 设置字体。
		grp.setFont(font);
		// 画边框。
		grp.setColor(getRandColor(20, 200));
		grp.drawRect(0, 0, width - 1, height - 1);
		grp.setColor(getRandColor(200, 250));
		grp.fillRect(1, 1, width - 1, height - 1);
		grp.setColor(new Color(102, 102, 102));
		grp.drawRect(0, 0, width - 1, height - 1);
		grp.setFont(font);

		grp.setColor(getRandColor(160, 200));
		// 画随机线
		for (int i = 0; i < 40; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			grp.drawLine(x, y, x + xl, y + yl);
		}

		// 从另一方向画随机线
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(12) + 1;
			int yl = random.nextInt(6) + 1;
			grp.drawLine(x, y, x - xl, y - yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer sb = new StringBuffer();
		// 随机产生codeCount数字的验证码。
		// int red,blue,greed;
		int s = codeSequence.length;
		for (int i = 0; i < codeCount; i++) {
			String code = String.valueOf(codeSequence[random.nextInt(s)]);
			int red = random.nextInt(100);
			int blue = random.nextInt(80);
			int greed = random.nextInt(160);

			grp.setColor(new Color(red, blue, greed));
			grp.drawString(code, (i + 1) * codeX, codeY);
			sb.append(code);
		}
		HttpSession session = getRequest().getSession();
		session.setAttribute("validateCode", sb.toString());
		getResponse().setContentType("image/jpeg");
		// 将图像输出到Servlet输出流中。
		try {
			ServletOutputStream sos = getResponse().getOutputStream();
			ImageIO.write(buffer, "jpeg", sos);
			sos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
