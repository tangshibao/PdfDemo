package com.zsx.pdfTemplate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker模板生成PDF工具类
 * @author ZSX
 */
public class PDFTemplateUtil {
	/**
	 * 
	 * @param templateDirPath 模板所在的文件夹的路径
	 * @param templateFileName 模板名称
	 * @param fontFilePath 字体路径
	 * @param generatedPdfPath 生成的pdf的路径
	 * @param data 填充数据
	 * @param encoding 编码，一般为UTF-8
	 */
	public static void createMainPdf(String templateDirPath,String templateFileName,String fontFilePath,String generatedPdfPath, Map<String, Object> data,String encoding) {
		
		OutputStream out = null;
		try {
			/**
			 * 生成PDF第一步：将数据填充到
			 */
	        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);//创建一个FreeMarker实例, 负责管理FreeMarker模板的Configuration实例
	        cfg.setDirectoryForTemplateLoading(new File(templateDirPath));// 指定FreeMarker模板文件的位置
            cfg.setEncoding(Locale.CHINA, encoding);// 设置模板的编码格式
            Template template = cfg.getTemplate(templateFileName, encoding);// 获取模板文件 template.ftl
            StringWriter writer = new StringWriter();
            template.process(data, writer);//template将data中数据填充并将数据流写入StringWriter
            writer.flush();
            String html = writer.toString();//得到已经填充好数据的html字符串
            
            /**
             * 使用ITextRenderer来生成pdf
             */
            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont(fontFilePath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 设置 css中 的字体样式（暂时仅支持宋体和黑体）
            renderer.setDocumentFromString(html);// 把html代码传入渲染器中
            renderer.layout();
            out = new FileOutputStream(new File(generatedPdfPath));
            renderer.createPDF(out);
            renderer.finishPDF();
            out.flush();
            out.close();
            System.out.println("生成PDF完成");
        } catch (Exception e) {
        	System.out.println("生成主PDF失败！！");
            e.printStackTrace();
            if(out != null){
        		try {
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
        	}
        }
    }
}
