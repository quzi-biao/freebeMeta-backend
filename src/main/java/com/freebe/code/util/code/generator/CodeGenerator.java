package com.freebe.code.util.code.generator;

import lombok.Data;

@Data
public abstract class CodeGenerator {
	private String entityName;
	
	private String srcPath;
	
	private String ctrollerPkg;
	
	private String servicePkg;
	
	private String paramPkg;
	
	private String entityPkg;
	
	private String voPkg;
	
	private String entityComment;

	private String serviceImplPkg;
	
	private String repositoryPkg;
	
	public abstract void generate() throws Exception;
}
