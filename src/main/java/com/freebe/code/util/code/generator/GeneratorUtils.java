package com.freebe.code.util.code.generator;

import java.io.File;

public class GeneratorUtils {
	public static String buildFileName(String srcPath, String pkg, String entityName, String suffix) {
		String path = getPath(srcPath, pkg);
		
		String name = path + '/' + entityName + suffix + ".java";
		return name;
	}

	public static String getPath(String srcPath, String path) {
		path = path.replaceAll("\\.", "/");
		if(!srcPath.endsWith("/")) {
			srcPath += '/';
		}
		String ret = srcPath + path;
		File f = new File(ret);
		if(!f.exists()) {
			f.mkdirs();
		}
		return ret;
	}
}
