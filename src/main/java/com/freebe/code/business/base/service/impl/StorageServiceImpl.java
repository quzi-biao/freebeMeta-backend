package com.freebe.code.business.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.freebe.code.business.base.entity.FileObject;
import com.freebe.code.business.base.service.StorageService;
import com.freebe.code.common.CustomException;
import com.freebe.code.web.config.OssConfig;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
	
	@Autowired
	private OssConfig ossConfig;
	
	private String bucketName;
    
    private OSS client;
    
    @PostConstruct
    public void init() {
    	this.bucketName = ossConfig.getBucketName();
    	client = new OSSClientBuilder().build(
    			ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
    	
    }
    
	@Override
	public String uploadFile(MultipartFile file, boolean forcedPut) throws CustomException {
		try {
			String originFileName = file.getOriginalFilename();
			if(originFileName.length() > 32) {
				if(originFileName.indexOf('.') >= 0) {
		        	String fileType = originFileName.substring(originFileName.lastIndexOf('.'));
		        	if(fileType.length() > 10) {
		        		originFileName = originFileName.substring(0, 32);
		        	}else {
		        		originFileName = originFileName.substring(0, 32-fileType.length()) + fileType;
		        	}
		        }else {
		        	originFileName = originFileName.substring(0, 32);
		        }
			}
			byte[] data = StreamUtils.copyToByteArray(file.getInputStream());
			StringBuffer keyBuf = new StringBuffer();
			keyBuf.append(data.length).append('_');
			String md5 = MD5.create().digestHex(data);
			keyBuf.append(md5.substring(0, 18)).append('_');
			keyBuf.append(originFileName);
			
	        String key = keyBuf.toString();
	        
    		if(!forcedPut && client.doesObjectExist(bucketName, key)) {
               return key;
            }
    		
			client.putObject(bucketName, key, file.getInputStream());
			
			  return key;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CustomException("上传文件失败：" + e.getMessage());
		}
	}

	@Override
	public List<String> batchUploadFile(MultipartFile[] files, boolean forcedPut) throws CustomException {
		if(null == files || files.length == 0) {
			return null;
		}
		List<String> ret = new ArrayList<>();
		for(MultipartFile file : files) {
			ret.add(this.uploadFile(file, forcedPut));
		}
		return ret;
	}

	@Override
	public FileObject downloadFile(String name) throws CustomException {
		try {
            if(!client.doesObjectExist(bucketName, name)) {
                throw new CustomException("文件不存在");
            }
            OSSObject ossObject = client.getObject(bucketName, name);
            return new FileObject(name, null, ossObject.getObjectContent());
        }catch (Exception e) {
            throw new CustomException("文件获取失败", e);
        }
	}

    
}
