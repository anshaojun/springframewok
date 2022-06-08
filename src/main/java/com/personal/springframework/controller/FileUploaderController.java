package com.personal.springframework.controller;

import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.enums.BizCodeEnum;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author anshaojun
 * @date 2022/6/8 0008 15:54
 * @description
 */
@RestController
@RequestMapping("fileupload")
public class FileUploaderController {

    @PostMapping("upload")
    public ResponseResult upload(MultipartFile[] files) {
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(),BizCodeEnum.SUCCESS.getMsg()).put("id",11111111);
    }
}
