package com.freebe.code.business.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.business.base.entity.FileObject;
import com.freebe.code.business.base.service.StorageService;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author xiezhengbiao
 *
 */
@Api(tags = "文件接口")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/storage")
public class StorageController extends AbstractController {

	@Autowired
    private StorageService storageService;

    /**
     * 上传文件接口，需要校验权限
     * @param file 上传文件
     * @return 上传文件访问路径
     * @throws CustomException 
     */
	@ApiOperation(value = "上传", notes = "文件上传接口")
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultBean<String> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "forcedPut", required = false) Boolean forcedPut
    ) throws CustomException {
    	if(null == forcedPut) {
    		forcedPut = true;
    	}
        return ResultBean.ok("", storageService.uploadFile(file, forcedPut));
    }
	
	  /**
     * 上传文件接口，需要校验权限
     * @param file 上传文件
     * @return 上传文件访问路径
     * @throws CustomException 
     */
	@ApiOperation(value = "上传", notes = "文件上传接口")
    @PostMapping(value = "upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JSONObject upload2(
            @RequestPart("upload") MultipartFile upload,
            @RequestParam(value = "forcedPut", required = false) Boolean forcedPut
    ) throws CustomException {
    	if(null == forcedPut) {
    		forcedPut = true;
    	}
        String url = storageService.uploadFile(upload, forcedPut);
        
        JSONObject jo = new JSONObject();
        jo.put("uploaded", 1);
        jo.put("url", "https://water-dev-test.oss-cn-hangzhou.aliyuncs.com/" + url);
        
        return jo;
    }


    /**
     * 批量上传文件接口，需要校验权限
     * @param files 上传文件
     * @return 上传文件信息
     * @throws CustomException 
     */
    @ApiOperation(value = "批量上传", notes = "文件批量上传接口")
    @PostMapping(value = "uploadBatch")
    public ResultBean<List<String>> uploadBatch(
            @RequestParam("files") MultipartFile[] files,
           @RequestParam(value = "forcedPut", required = false) boolean forcedPut
    ) throws CustomException {
        List<String> objectList = storageService.batchUploadFile(files, forcedPut);
        return ResultBean.ok(objectList);
    }

    /**
     * 下载文件接口，需要校验权限
     * @param request
     * @param response
     * @throws CustomException 
     */
    @ApiOperation(value = "下载", notes = "文件下载接口")
    @GetMapping("download")
    public void download(@RequestParam String name) throws CustomException {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	HttpServletResponse response = attributes.getResponse();
    	
    	FileObject object = storageService.downloadFile(name);
    	write(response, object.getFileName(), object.getInputStream(), object.getMimeType());
    }


}

