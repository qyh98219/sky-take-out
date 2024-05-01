package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CommonController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/1 20:20
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/common")
@Tag(name = "通用接口")
public class CommonController {
    @Value("${sky.fileServer.rootPath}")
    private String saveRootPath;
    @Value("${sky.fileServer.reqRootPath}")
    private String reqRootPath;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestPart("file")MultipartFile uploadFile){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String fileName= uploadFile.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        String saveFileName = uuid + "." + fileExt;
        String saveFilePath = saveRootPath + File.separator + saveFileName;
        File saveFile = new File(saveFilePath);
        try {
            uploadFile.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String reqPath = reqRootPath + File.separator + saveFileName;
        return Result.success(reqPath);
    }
}
