package com.personal.springframework.controller;

import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.UploadFile;
import com.personal.springframework.model.enums.AjaxResultEnum;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author anshaojun
 * @date 2022/6/8 0008 15:54
 * @description
 */
@RestController
@RequestMapping("fileUpload")
public class FileUploaderController {

    @Value("${resolver.filePath}")
    private String filePath;
    @Value("${resolver.domain}")
    private String domain;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseResult upload(UploadFile fileDto, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        File uploadFile = new File(filePath + File.separator + fileDto.getUuid(), fileDto.getFileName().substring(0,fileDto.getFileName().lastIndexOf("."))+"_" + fileDto.getIndex());

        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }

        if (fileDto.getIndex() < fileDto.getTotal()) {
            try {
                fileDto.getFile().transferTo(uploadFile);
                return ResponseResult.result(AjaxResultEnum.STEP_SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseResult.result(AjaxResultEnum.UPLOAD_FAIL);
            }
        } else {
            try {
                fileDto.getFile().transferTo(uploadFile);
                return ResponseResult.success("上传完毕");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseResult.result(AjaxResultEnum.UPLOAD_FAIL);
            }
        }
    }

    @GetMapping("/merge")
    @ResponseBody
    public ResponseResult merge(String uuid, String newFileName, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        try {
            File dirFile = new File(filePath + File.separator + uuid);
            if (!dirFile.exists()) {
                throw new ServiceException("文件不存在！");
            }
            //分片上传的文件已经位于同一个文件夹下，方便寻找和遍历（当文件数大于十的时候记得排序用冒泡排序确保顺序是正确的）
            String[] fileNames = dirFile.list();
            String name = newFileName.substring(0, newFileName.lastIndexOf("."));
            Arrays.sort(fileNames, (o1, o2) -> {
                int i1 = Integer.parseInt(o1.split("_")[1]);
                int i2 = Integer.parseInt(o2.split("_")[1]);
                return i1 - i2;
            });

            //创建空的合并文件
            File targetFile = new File(dirFile, newFileName);

            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw");

            long position = 0;
            for (String fileName : fileNames) {
                System.out.println(fileName);
                File sourceFile = new File(filePath + File.separator + uuid, fileName);
                RandomAccessFile readFile = new RandomAccessFile(sourceFile, "rw");
                int chunksize = 1024 * 3;
                byte[] buf = new byte[chunksize];
                writeFile.seek(position);
                int byteCount;
                while ((byteCount = readFile.read(buf)) != -1) {
                    if (byteCount != chunksize) {
                        byte[] tempBytes = new byte[byteCount];
                        System.arraycopy(buf, 0, tempBytes, 0, byteCount);
                        buf = tempBytes;
                    }
                    writeFile.write(buf);
                    position = position + byteCount;
                }
                readFile.close();
                FileUtils.deleteQuietly(sourceFile);//删除缓存的临时文件
            }
            writeFile.close();
            return ResponseResult.success("合并成功").put("filePath:", domain + filePath + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.result(AjaxResultEnum.MERGE_FAIL);
        }
    }


}
