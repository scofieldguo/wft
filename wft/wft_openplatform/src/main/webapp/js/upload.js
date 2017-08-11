/**
 * 图片上传接口
 * 
 * @param button
 *            上传-button对象（jQuery对象）
 * @param img_addr
 *            图片保存地址-text对象（jQuery对象）
 * @param img_preview
 *            图片预览-img对象（jQuery对象）
 * @param prompt
 *            异常提示语
 * @param server_addr
 *            服务器地址
 * @param height
 * @param width
 * @return
 */
function imgUpload(button, img_addr, img_preview, prompt, server_addr, height,width) {
	new AjaxUpload(
			button,
			{
				action : "uploadAction.action?height=" + height + "&width="
						+ width,
				autoSubmit : true,
				name : 'source',// 文件对象名称（不是文件名）
				onSubmit : function(file, ext) {
					if (ext&& /^(jpg|png|jpeg|gif|JPG|PNG|JPEG|GIF)$/.test(ext)) {
						this.setData( {
							'fileType' : ext
						});
						prompt.css('display', 'none');
					} else {
						prompt.text('文件格式错误:只能上传图片类型 ,正确格式为: .jpg .png .jpeg .gif,请重新上传!');
						prompt.css('display', 'inline');
						return false;
					}
				},
				onComplete : function(file, response) {
					var reg = /<pre/;
					if (reg.exec(response)) {/* fiefox */
						var tempVal = response.substring(
								response.indexOf('>') + 1, response
										.lastIndexOf('<'));
					} else {/* IE */
						var tempVal = response;
					}
					if (tempVal == "error") {
						alert("图片尺寸不正确，请重新上传");
					} else {
						img_addr.val('uploadImage/' + tempVal);
						img_preview.css("display", "block");
						img_preview.attr('src', server_addr + '/uploadImage/'
								+ tempVal);
					}
				}
			});
}
/*
 * function imageUpload(server_addr) { imgUpload($("#upload_btn"),
 * $('#image_addr'), $('#image_preview'), server_addr); }
 */