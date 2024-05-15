package com.personal.springframework.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFile {
    private MultipartFile file;
    //是否分片上传
    private boolean piece;
    //文件名称(包含文件后缀)
    private String fileName;
    //文件总分片数
    private int total;
    //分片文件索引下标
    private int index;
    //唯一标识
    private String uuid;
}
