package com.freebe.code.business.base.vo;

import org.apache.poi.ss.usermodel.Workbook;

import lombok.Data;

@Data
public class ExportFile {
	
	private Workbook book;
	
	private String filename;
}
