package com.zsx.pdfTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UtilTest {
	
	public static void main(String[] args) throws Exception {
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("imgPath", "E:/PersonalData/Desktop/pdfTest/img/cat.jpg");
		data.put("name", "张三");
		data.put("age", 17);
		data.put("gender", "男");
		data.put("salary", 6040.56);
		data.put("title", "标题");
		data.put("leaveWord", "今年是我国改革开放40周年。1978年12月，党的十一届三中全会作出把党和国家工作中心转移到经济建设上来、实行改革开放的历史性决策，动员全党全国各族人民为社会主义现代化建设进行新的长征。这是新中国成立以来我们党和国家历史上具有深远意义的伟大转折，是决定当代中国命运的关键抉择。40年来，我们党团结带领人民，艰苦奋斗、顽强拼搏，坚决破除阻碍国家和民族发展的思想束缚和体制障碍，开辟了中国道路，释放了中国活力，凝聚了中国力量，实现了从赶上时代到引领时代的伟大跨越，书写了国家和民族发展的壮丽史诗，党的面貌、国家的面貌、人民的面貌、军队的面貌、中华民族的面貌发生了前所未有的变化。我国改革开放的伟大创举，也深刻影响了世界，为世界各国带来巨大机遇。党的十八大以来，以习近平同志为核心的党中央以巨大的政治勇气和强烈的责任担当，全面深化改革，扩大对外开放，党和国家事业取得历史性成就、发生历史性变革，推动中国特色社会主义进入新时代，掀开了改革开放新的历史篇章，中华民族正以更加崭新的姿态屹立于世界东方。");
		
		String templateDirPath = "E:\\PersonalData\\Desktop\\pdfTest\\template\\";
		String templateFileName = "template.ftl";
		String fontFilePath = "E:\\PersonalData\\Desktop\\pdfTest\\font\\simsun.ttc";
		
		String generatedPdfPath = "E:\\PersonalData\\Desktop\\pdfTest\\pdfs\\temp.pdf";
		String encoding = "UTF-8";
		PDFTemplateUtil.createMainPdf(templateDirPath, templateFileName, fontFilePath, generatedPdfPath, data, encoding);
	}

}
