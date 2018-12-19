package com.zsx.pdfTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class CombineTest {
	public static void main(String[] args) {
		CombineTest combineTest = new CombineTest();
		combineTest.combinePdfs3();
	}

	/**
	 * 
	 * @param createParams
	 */
	public void combinePdfs3() {
		String downloadPdfPath = "E:/PersonalData/Desktop/pdfTest/final/combine.pdf";// 下载路径：pdf最终生成的路径
		String mainPdfPath = "E:/PersonalData/Desktop/pdfTest/pdfs/temp.pdf";// 主pdf路径（模板路径）
		String explain2Path = "E:/PersonalData/Desktop/pdfTest/pdfs/doc1.pdf";
		String explain3Path = "E:/PersonalData/Desktop/pdfTest/pdfs/doc2.pdf";
		String fontPath = "E:/PersonalData/Desktop/pdfTest/font/simsun.ttc,0";//宋体
		String waterIconPath = "E:/PersonalData/Desktop/pdfTest/img/watermark.png";
		ByteArrayOutputStream mainBos;// 主pdf缓存流
		ByteArrayOutputStream explain2Bos;//待整合pdf1
		ByteArrayOutputStream explain3Bos;
		
		FileOutputStream out;// 输出流
		PdfReader mainReader;
		PdfReader explain2Reader;//第一个附件阅读者对象
		PdfReader explain3Reader;//第二个附件阅读者对象
		int totalPage = 0;
		int mainPdfPage = 0;
		int explain2Page = 0;
		int explain3Page = 0;
		int indexPage = 1;
		PdfStamper explainPs = null;// 将reader中的数据读入缓存数组中进行编辑
		try {
			mainBos = new ByteArrayOutputStream();// 主文档缓存流
			explain2Bos = new ByteArrayOutputStream();//附件文档1缓冲流
			explain3Bos = new ByteArrayOutputStream();//附加文档2缓冲流
			
			mainReader = new PdfReader(mainPdfPath);// 读入主文档
			explain2Reader = new PdfReader(explain2Path);
			explain3Reader = new PdfReader(explain3Path);
			/**
			 * 计算页数
			 */
			mainPdfPage = mainReader.getNumberOfPages();
			explain2Page = explain2Reader.getNumberOfPages();
			explain3Page = explain3Reader.getNumberOfPages();
			totalPage = mainPdfPage + explain2Page +  explain3Page;
			
			PdfStamper ps = new PdfStamper(mainReader, mainBos);// 将reader中的数据读入缓存数组中进行编辑
			
			
			PdfContentByte pcb = null;
			
			for(int i = 1;i <= mainPdfPage;i++){
				pcb = ps.getOverContent(i);//PdfContentByte：PDF内容对象，表示指定页的内容
				if(i == 1){
					setQRCode("code",pcb);//设置二维码
					setWaterIcon(waterIconPath,pcb);//设置水印
					//为主pdf设置页码
					setFooter(totalPage,indexPage,pcb,fontPath);
					indexPage++;
					
				}else if(i == 2){
					setWaterIcon(waterIconPath,pcb);
					setFooter(totalPage,indexPage,pcb,fontPath);
					indexPage++;
					/**
					 * 编辑整合进来的文档
					 */
					explainPs = new PdfStamper(explain2Reader, explain2Bos);//读入第二个文档
					for(int j = 1;j <= explain2Page;j++){
						pcb = explainPs.getOverContent(j);
						setWaterIcon(waterIconPath,pcb);//设置水印
						setFooter(totalPage,indexPage,pcb,fontPath);
						indexPage++;
					}
					explainPs.setFormFlattening(true);// 关闭
					explainPs.close();
					
					
					explainPs = new PdfStamper(explain3Reader, explain3Bos);//读入第二个文档
					for(int j = 1;j <= explain3Page;j++){
						pcb = explainPs.getOverContent(j);
						setWaterIcon(waterIconPath,pcb);//设置水印
						setFooter(totalPage,indexPage,pcb,fontPath);
						indexPage++;
					}
					explainPs.setFormFlattening(true);// 关闭
					explainPs.close();
					
					
				}else{
					setWaterIcon(waterIconPath,pcb);
					setFooter(totalPage,indexPage,pcb,fontPath);
					indexPage++;
				}
			}
			
			/**
			 * 关闭reader对象
			 */
			
			ps.setFormFlattening(true);// 关闭
			ps.close();
			
			
			
			
			/**
			 * 合成pdf
			 */
			out = new FileOutputStream(downloadPdfPath);// 指定生成位置
			Document doc = new Document();// 新建一个文档
			PdfCopy copy = new PdfCopy(doc, out);

			doc.open();
			PdfImportedPage page = null;
			// 获取模板的总页数
			for (int i = 1; i <= mainPdfPage; i++) {
				doc.newPage();

				page = copy.getImportedPage(new PdfReader(mainBos.toByteArray()), i);
				copy.addPage(page);

				// 第二页,此时中断追加
				if (i == 2) {
					for (int j = 1; j <= explain2Page; j++) {
						doc.newPage();
						page = copy.getImportedPage(new PdfReader(explain2Bos.toByteArray()), j);// 从当前Pdf,获取第j页
						copy.addPage(page);
					}
					for (int j = 1; j <= explain3Page; j++) {
						doc.newPage();
						page = copy.getImportedPage(new PdfReader(explain3Bos.toByteArray()), j);// 从当前Pdf,获取第j页
						copy.addPage(page);
					}
				}

			}
			explain2Reader.close();
			explain3Reader.close();
			mainReader.close();
			copy.close();
			doc.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 设置页码
	 * @param totalPage 总页数
	 * @param pcb 页内容对象
	 * @param indexPage 当前页码
	 */
	public void setFooter(int totalPage,int indexPage,PdfContentByte pcb,String fontPath){
		BaseFont bf = null;
		try{
			
			bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);//字体
			PdfGState gs = new PdfGState();//设置透明度
			gs.setFillOpacity(1.0f);//不透明度1：完全不透明
			pcb.setGState(gs);//透明度
			pcb.beginText();
			pcb.setFontAndSize(bf, 12);//字体大小设置为12
			pcb.setTextMatrix(250.0F, 10.0F);//设置位置
			pcb.showText("第" + indexPage + "页 /共" + totalPage + "页");
			pcb.endText();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * 设置二维码
	 * @param message
	 * @param pcb
	 */
	public void setQRCode(String message,PdfContentByte pcb){
		try {	
			BarcodeQRCode qrCode = new BarcodeQRCode("message", 100, 100, null);// 生成图片
			Image codeImage = qrCode.getImage();// 生成二维码图像
			codeImage.setAbsolutePosition(480.0F, 660.0F);// 设置位置
			codeImage.scaleAbsolute(82.0F, 82.0F);// 设置图片大小
			PdfGState gs = new PdfGState();//设置透明度
			gs.setFillOpacity(1.0f);//不透明度1：完全不透明
			pcb.setGState(gs);//透明度
			pcb.addImage(codeImage);// 将图片添加到文档中
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 设置水印
	 */
	public void setWaterIcon(String waterIconPath,PdfContentByte pcb){
		try {
			Image waterIcon = Image.getInstance(waterIconPath);//加载水印图片
			waterIcon.setAbsolutePosition(220.0F, 400.0F);// 设置位置
			waterIcon.scaleAbsolute(200F, 200F);// 设置图片大小
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.2f);//不透明度0.2
			pcb.setGState(gs);//透明度
			pcb.addImage(waterIcon);// 将图片添加到文档中
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
