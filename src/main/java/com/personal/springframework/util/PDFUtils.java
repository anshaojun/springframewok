package com.personal.springframework.util;

import com.google.code.appengine.awt.Color;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.personal.springframework.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class PDFUtils {

    private static Integer WATER_FONT_SIZE = 48;

    public static void addWaterText(String waterText, Integer fontsize, String filepath) throws ServiceException {
        try {
            if (StringUtils.isBlank(waterText)) {
                throw new ServiceException("水印文字为空");
            }
            File source = new File(filepath);
            if (!source.exists()) {
                throw new ServiceException("文件不存在");
            }
            // 待加水印的文件
            PdfReader reader = new PdfReader(filepath);
            // 加完水印的文件
            String sourceName = source.getName();
            String path = source.getPath().substring(0, source.getPath().lastIndexOf(File.separator));
            String tempName = path + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".pdf";
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tempName));
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;
            // 设置字体
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            // 循环对每页插入水印
            for (int i = 1; i < total; i++) {
                // 水印的起始
                content = stamper.getUnderContent(i);
                // 开始
                content.beginText();
                // 设置颜色
                content.setColorFill(Color.LIGHT_GRAY);
                // 设置字体及字号
                content.setFontAndSize(base, fontsize == null ? WATER_FONT_SIZE : fontsize);
                // 设置起始位置
                content.setTextMatrix(70, 200);
                content.showTextAligned(com.lowagie.text.Element.ALIGN_CENTER, waterText, 300, 350, 55);
                content.endText();
            }
            stamper.close();
            source.delete();
            File file = new File(tempName);
            file.renameTo(new File(path + File.separator + sourceName));
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
