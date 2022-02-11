package com.personal.springframework.util;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class JaxbUtil {
    public static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private static char[] encodeToChar(byte[] arr, boolean lineSeparator) {
        int len = arr != null ? arr.length : 0;
        if (len == 0) {
            return new char[0];
        } else {
            int evenlen = len / 3 * 3;
            int cnt = (len - 1) / 3 + 1 << 2;
            int destLen = cnt + (lineSeparator ? (cnt - 1) / 76 << 1 : 0);
            char[] dest = new char[destLen];
            int left = 0;
            int d = 0;
            int cc = 0;

            while (left < evenlen) {
                int i = (arr[left++] & 255) << 16 | (arr[left++] & 255) << 8 | arr[left++] & 255;
                dest[d++] = CHARS[i >>> 18 & 63];
                dest[d++] = CHARS[i >>> 12 & 63];
                dest[d++] = CHARS[i >>> 6 & 63];
                dest[d++] = CHARS[i & 63];
                if (lineSeparator) {
                    ++cc;
                    if (cc == 19 && d < destLen - 2) {
                        dest[d++] = '\r';
                        dest[d++] = '\n';
                        cc = 0;
                    }
                }
            }

            left = len - evenlen;
            if (left > 0) {
                d = (arr[evenlen] & 255) << 10 | (left == 2 ? (arr[len - 1] & 255) << 2 : 0);
                dest[destLen - 4] = CHARS[d >> 12];
                dest[destLen - 3] = CHARS[d >>> 6 & 63];
                dest[destLen - 2] = left == 2 ? CHARS[d & 63] : 61;
                dest[destLen - 1] = '=';
            }

            return dest;
        }
    }

    private static String encodeToString(String s, String encoding) {
        try {
            return new String(encodeToChar(s.getBytes(encoding), false));
        } catch (UnsupportedEncodingException var2) {
            return null;
        }
    }

    /**
     * JavaBean装换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String converToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            StringWriter writer = new StringWriter();
            writer.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
            //获得Marshaller对象
            Marshaller marshaller = context.createMarshaller();
            //格式化xml格式
            //marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //去掉生成xml的默认报文头
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            //java 对象生成到writer
            marshaller.marshal(obj, writer);
            //转化为String类型
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String converToXmlBase64(Object obj, String encoding) {
        return encodeToString(converToXml(obj, encoding), encoding);
    }

    /**
     * xml装换成JavaBean
     *
     * @param xml
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T converyToJavaBean(String xml, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;

    }

}