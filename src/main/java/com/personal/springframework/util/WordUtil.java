package com.personal.springframework.util;


import com.aspose.words.Document;
import com.aspose.words.License;
import com.personal.springframework.exception.ServiceException;
import com.spire.doc.Section;
import javafx.print.PageOrientation;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WordUtil {

    static {
        License aposeLic = new License();
        InputStream license = WordUtil.class.getResourceAsStream("/license/aspose-license.xml");
        try {
            aposeLic.setLicense(license);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return void
     * @Author 安少军
     * @Description doc转docx
     * @Date 12:11 2021/11/26
     * @Param [doc]
     **/
    private static void doc2docx(String doc) throws Exception {
        Document dc = new Document(doc);
        dc.save(doc + "x");
    }

    /**
     * @return void
     * @Author 安少军
     * @Description word转pdf文件输出
     * @Date 11:19 2021/11/26
     * @Param [source, exportPath]
     **/
    public static void word2pdf(String source, String exportPath, String waterText, Integer waterSize, boolean deleteTrans) throws ServiceException {
        boolean delTemp = false;
        String pdf = null;
        try {
            File file = new File(source);
            if (!file.exists()) {
                throw new ServiceException("源文件不存在！");
            }
            if (!new File(exportPath).exists()) {
                throw new ServiceException("输出路径不存在！");
            }
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!suffix.equals("doc") && !suffix.equals("docx")) {
                throw new ServiceException("文件格式错误！");
            }
            if (suffix.equals("doc")) {
                doc2docx(source);
                source = source + "x";
                delTemp = true;
            }
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            pdf = exportPath + File.separator + name + ".pdf";
            Document doc = new Document(source); //Address是将要被转化的word文档
            OutputStream os = Files.newOutputStream(Paths.get(pdf));
            doc.save(os, com.aspose.words.SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            os.close();
            if (StringUtils.isNotBlank(waterText)) {
                PDFUtils.addWaterText(waterText, waterSize, exportPath + File.separator + name + ".pdf");
            }
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            if (delTemp) {
                File temp = new File(source);
                if (temp.exists()) {
                    temp.delete();
                }
            }
            if (deleteTrans) {
                try {
                    Files.deleteIfExists(Paths.get(pdf));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @return void
     * @Author 安少军
     * @Description word转pdf流输出
     * @Date 11:18 2021/11/26
     * @Param [source, outPDF]
     **/
    public static void word2pdf(String source, OutputStream outPDF, String waterText, Integer waterSize, boolean deleteTrans) throws ServiceException {
        boolean delTemp = false;
        String pdf = null;
        try {
            File file = new File(source);
            if (!file.exists()) {
                throw new ServiceException("源文件不存在！");
            }
            String fileName = file.getName();
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            String path = file.getPath().substring(0, file.getPath().lastIndexOf(File.separator));
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!suffix.equals("doc") && !suffix.equals("docx")) {
                throw new ServiceException("文件格式错误！");
            }
            if (suffix.equals("doc")) {
                doc2docx(source);
                source = source + "x";
                delTemp = true;
            }
            pdf = path + File.separator + name + ".pdf";
            OutputStream outputStream = Files.newOutputStream(Paths.get(pdf));
            Document doc = new Document(source);
            doc.save(outputStream, com.aspose.words.SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            outputStream.close();
            if (StringUtils.isNotBlank(waterText)) {
                PDFUtils.addWaterText(waterText, waterSize, pdf);
            }
            InputStream inputStream = new FileInputStream(pdf);
            byte[] buf = new byte[inputStream.available()];
            inputStream.read(buf);
            inputStream.close();
            outPDF.write(buf);
            outPDF.flush();
            outPDF.close();
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            if (delTemp) {
                File temp = new File(source);
                if (temp.exists()) {
                    temp.delete();
                }
            }
            if (deleteTrans) {
                try {
                    Files.deleteIfExists(Paths.get(pdf));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
