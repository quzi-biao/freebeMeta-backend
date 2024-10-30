package com.freebe.code.util.code.generator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.controller.param.BaseEntityParam;
import com.freebe.code.business.base.repository.BaseRepository;
import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.PageBean;
import com.freebe.code.util.code.generator.doclet.SrcFileWriter;
import com.freebe.code.util.code.generator.generator.ControllerGenerator;
import com.freebe.code.util.code.generator.generator.ServiceImplGenerator;
import com.freebe.code.util.code.generator.generator.VOClassGenerator;


/**
 * 代码生成
 * @author xiezhengbiao
 *
 */
public class GodeGenerator {
	private static String srcPath = "/Users/zhengbiaoxie/Workspace/freebe.meta/meta-be/src/main/java";
	private static String basePath = "com.freebe.code.business.meta";
	
	private static String entityPath = basePath + ".entity";
	private static String repositoryPath = basePath + ".repository";
	private static String servicePath = basePath + ".service";
	private static String serviceImplPath = basePath +".service.impl";
	private static String voPath = basePath +".vo";
	private static String paramPath = basePath +".controller.param";
	private static String controllerPath = basePath +".controller";
	private static String[] ENTITIES = new String[] {"TaskType"};
	
	public static void main(String[] args) throws Exception {
		System.out.println(GeneratorUtils.getPath(srcPath, entityPath));
		File[] entities = new File(GeneratorUtils.getPath(srcPath, entityPath)).listFiles();
		Set<String> createEntities = new HashSet<>(Arrays.asList(ENTITIES));
		for(File entity : entities) {
			if(entity.isDirectory()) {
				continue;
			}
			String name = entity.getName();
			String entityName = name.substring(0, name.indexOf('.'));
			if(createEntities.contains(entityName)) {
				createFiles(entityName);
			}
		}
		
	}
	
	private static void createFiles(String entityName) throws Exception {
		System.out.println("为 " + entityName + " 创建文件");
		createRepository(entityName);
		createVOs(entityName);
		createService(entityName);
		createController(entityName);
		createServiceImpl(entityName);
	}
	
	/**
	 * 创建 VO 类
	 * @param entityName
	 * @throws IOException
	 */
	private static void createVOs(String entityName) throws Exception {
		String entityClass = entityPath + '.' + entityName;
		VOClassGenerator voGenerator = new VOClassGenerator(srcPath, entityClass, voPath, "VO", BaseVO.class.getName());
		voGenerator.generate();
		VOClassGenerator paramGenerator = new VOClassGenerator(srcPath, entityClass, paramPath, "Param", BaseEntityParam.class.getName());
		paramGenerator.generate();
		VOClassGenerator queryGenerator = new VOClassGenerator(srcPath, entityClass, paramPath, "QueryParam", PageBean.class.getName());
		queryGenerator.generate();
	}
	
	private static void createServiceImpl(String entityName) throws Exception {
		ServiceImplGenerator cg = new ServiceImplGenerator();
		cg.setCtrollerPkg(controllerPath);
		cg.setEntityName(entityName);
		cg.setParamPkg(paramPath);
		cg.setServicePkg(servicePath);
		cg.setSrcPath(srcPath);
		cg.setVoPkg(voPath);
		cg.setEntityPkg(entityPath);
		cg.setServiceImplPkg(serviceImplPath);
		cg.setRepositoryPkg(repositoryPath);
		
		cg.generate();
	}
	
	/**
	 * 创建 Service 类
	 * @param entityName
	 * @throws Exception
	 */
	private static void createController(String entityName) throws Exception {
		ControllerGenerator cg = new ControllerGenerator();
		cg.setCtrollerPkg(controllerPath);
		cg.setEntityName(entityName);
		cg.setParamPkg(paramPath);
		cg.setServicePkg(servicePath);
		cg.setSrcPath(srcPath);
		cg.setVoPkg(voPath);
		cg.setEntityPkg(entityPath);
		
		cg.generate();
		
	}
	
	/**
	 * 创建 Service 类
	 * @param entityName
	 * @throws Exception
	 */
	private static void createService(String entityName) throws Exception {
		String fileName = GeneratorUtils.buildFileName(srcPath, servicePath, entityName, "Service");
		if(new File(fileName).exists()) {
			System.out.println("Service 已存在：" + fileName);
			return;
		}
		
		System.out.println("创建文件：" + fileName);
		SrcFileWriter writer = new SrcFileWriter(fileName);
		
		writer.writePackage(servicePath);
		writer.writeEmptyLine();
		writer.writeImport(Page.class.getName());
		writer.writeEmptyLine();
		writer.writeImport(BaseService.class.getName());
		writer.writeImport(CustomException.class.getName());
		writer.writeImport(entityPath + "." + entityName);
		writer.writeImport(voPath + "." + entityName + "VO");
		writer.writeImport(paramPath + "." + entityName + "Param");
		writer.writeImport(paramPath + "." + entityName + "QueryParam");
		writer.writeEmptyLine();
		
		writer.write("/**");
		writer.write(" *");
		writer.write(" * @author zhengbiaoxie");
		writer.write(" *");
		writer.write(" */");
		
		StringBuffer buf = new StringBuffer("public interface ");
		buf.append(entityName).append("Service extends BaseService<").append(entityName).append("> {");
		writer.write(buf.toString());
		writer.writeEmptyLine();
		
		buf.setLength(0);
		buf.append('\t');
		buf.append(entityName).append("VO findById(").append("Long id) throws CustomException;");
		writer.write(buf.toString());
		writer.writeEmptyLine();
		
		buf.setLength(0);
		buf.append('\t');
		buf.append(entityName).append("VO createOrUpdate(").append(entityName).append("Param").append(" param) throws CustomException;");
		writer.write(buf.toString());
		writer.writeEmptyLine();
		
		buf.setLength(0);
		buf.append('\t');
		buf.append("Page<").append(entityName).append("VO> queryPage(").append(entityName).append("QueryParam").append(" param) throws CustomException;");
		writer.write(buf.toString());
		writer.writeEmptyLine();
		
		writer.write("}");
		
		writer.flush();
		writer.close();
	}
	
	/**
	 * 创建 Repository 类
	 * @param entityName
	 * @throws IOException
	 */
	private static void createRepository(String entityName) throws IOException {
		String fileName = GeneratorUtils.buildFileName(srcPath, repositoryPath, entityName, "Repository");
		if(new File(fileName).exists()) {
			System.out.println("Repository 已存在：" + fileName);
			return;
		}
		System.out.println("创建文件：" + fileName);
		SrcFileWriter writer = new SrcFileWriter(fileName);
		
		writer.writePackage(repositoryPath);
		writer.writeEmptyLine();
		writer.write("import org.springframework.stereotype.Repository;");
		writer.writeImport(entityPath + '.' + entityName);
		writer.writeImport(BaseRepository.class.getName());
		writer.writeEmptyLine();
		
		writer.write("/**");
		writer.write(" *");
		writer.write(" * @author zhengbiaoxie");
		writer.write(" *");
		writer.write(" */");
		
		writer.write("@Repository");
		
		StringBuffer buf = new StringBuffer("public interface ");
		buf.append(entityName).append("Repository extends BaseRepository<").append(entityName).append("> {");
		writer.write(buf.toString());
		writer.writeEmptyLine();
		writer.write("}");
		
		writer.flush();
		writer.close();
	}

}
