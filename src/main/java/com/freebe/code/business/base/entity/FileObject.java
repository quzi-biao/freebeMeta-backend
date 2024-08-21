package com.freebe.code.business.base.entity;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileObject {
	private String fileName;
	
	private String mimeType;
	
	private InputStream inputStream;
	
}
