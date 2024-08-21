package com.freebe.code.business.base.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.freebe.code.business.base.entity.FileObject;
import com.freebe.code.common.CustomException;

/**
 * 存储服务
 * @author xiezhengbiao
 *
 */
public interface StorageService {

	/**
	 * 上传文件
	 * @param prefix
	 * @param file
	 * @param forcedPut
	 * @return
	 * @throws CustomException 
	 */
	String uploadFile(MultipartFile file, boolean forcedPut) throws CustomException;
	
	/**
	 * 批量是上传
	 * @param prefix
	 * @param files
	 * @param forcedPut
	 * @return
	 * @throws CustomException 
	 */
	List<String> batchUploadFile(MultipartFile[] files, boolean forcedPut) throws CustomException;

	/**
	 * 下载文件
	 * @param name
	 * @return
	 * @throws CustomException 
	 */
	FileObject downloadFile(String name) throws CustomException;

}
