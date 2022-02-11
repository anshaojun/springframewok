package com.personal.springframework.controller;


import com.personal.springframework.annotation.EscapeHtmlString;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @program: personalhub
 * @description: 父controller
 * @author: 安少军
 * @create: 2021-12-21 10:36
 **/
public class AbstractController {

    protected void escapeHtmlString(Object clas) {
        Class<?> clazz = clas.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object val = field.get(clas);
                if (val != null && val instanceof String) {
                    boolean filedHasAnno = field.isAnnotationPresent(EscapeHtmlString.class);
                    if (filedHasAnno) {
                        field.set(clas, StringEscapeUtils.unescapeHtml4(val.toString()));
                    }
                } else if (val instanceof List) {
                    if (val != null) {
                        List<T> items = (List) val;
                        for (int i = 0; i < items.size(); i++) {
                            escapeHtmlString(items.get(i));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
