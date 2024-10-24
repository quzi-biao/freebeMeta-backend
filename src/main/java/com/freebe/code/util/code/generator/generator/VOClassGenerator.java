package com.freebe.code.util.code.generator.generator;

import java.io.File;
import java.io.IOException;

import com.freebe.code.business.base.entity.BaseEntity;
import com.freebe.code.business.base.entity.CodeEntity;
import com.freebe.code.business.base.entity.IdEntity;
import com.freebe.code.util.code.generator.CodeGenerator;
import com.freebe.code.util.code.generator.GeneratorUtils;
import com.freebe.code.util.code.generator.doclet.Doclet;
import com.freebe.code.util.code.generator.doclet.FieldEntry;
import com.freebe.code.util.code.generator.doclet.ModelClassDocVO;
import com.freebe.code.util.code.generator.doclet.SrcFileWriter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 生成 VO 代码
 * @author xiezhengbiao
 *
 */
public class VOClassGenerator extends CodeGenerator {
	
	private String srcPath;
	
	private Class<?> entityClass;
	
	private String voPkg;
	
	private String entityName;
	
	private SrcFileWriter writer = null;
	
	private String fileName = null;
	
	private String entryFilePath = null;
	
	private String suffix = null;
	
	private String parentClassName = null;
	
	private String parentSimpleClassName = null;
	
	private Class<?> endParentClass = null;
	
	public VOClassGenerator(String srcPath, String entityClass, String voPkg, String suffix, String parentClassName) throws Exception {
		this.entityClass = Class.forName(entityClass);
		this.endParentClass = Class.forName(parentClassName);
		
		this.voPkg = voPkg;
		this.srcPath = srcPath;
		this.entityName = this.entityClass.getSimpleName();
		this.suffix = suffix;
		this.parentClassName = parentClassName;
		this.parentSimpleClassName = parentClassName.substring(parentClassName.lastIndexOf(".") + 1);
		
		int index = entityClass.lastIndexOf('.');
		this.entryFilePath = GeneratorUtils.buildFileName(this.srcPath, entityClass.substring(0, index), entityClass.substring(index + 1), "");
		this.fileName = GeneratorUtils.buildFileName(this.srcPath, voPkg, this.entityClass.getSimpleName(), suffix);
		if(!new File(fileName).exists()) {
			writer = new SrcFileWriter(fileName);
		}
	}
	
	public void generate() throws Exception {
		Class<?> parent = this.entityClass.getSuperclass();
		if(!(parent.equals(BaseEntity.class) || parent.equals(CodeEntity.class) || parent.equals(IdEntity.class) || parent.equals(Object.class))) {
			VOClassGenerator g = new VOClassGenerator(srcPath, parent.getName(), voPkg, suffix, this.endParentClass.getName());
			g.generate();
			this.parentClassName = voPkg + "." + parent.getSimpleName() + suffix;
			this.parentSimpleClassName = parent.getSimpleName() + suffix;
		}
		
		if(writer == null) {
			System.out.println("VO 已存在：" + fileName);
			return;
		}
		
		System.out.println("创建文件：" + fileName);
		
		Doclet doclet = new Doclet(entryFilePath);
        ModelClassDocVO modelClassDocVO = doclet.exec();
        
		writeImport(modelClassDocVO);
		
		writer.write("@Data");
		writer.write("@EqualsAndHashCode(callSuper = false)");
		writer.write("@NoArgsConstructor");
		
		String comment = modelClassDocVO.getModelCommentText();
		if(suffix.equals("Param")) {
			comment += "参数";
		}
		if(suffix.equals("QueryParam")) {
			comment += "查询参数";
		}
		
		writer.write("@ApiModel(\"" + comment + "\")");
		
		StringBuffer buf = new StringBuffer("public class ");
		buf.append(entityName).append(suffix).append(" extends ").append(parentSimpleClassName).append(" {");
		writer.write(buf.toString());
		
		writerFields(modelClassDocVO);
		
		writer.writeEmptyLine();
		writer.write("}");
		
		writer.flush();
		writer.close();
	}
	
	private void writerFields(ModelClassDocVO modelClassDocVO) throws Exception {
		if(null == modelClassDocVO.getFieldEntryList()) {
			return;
		}
		for(FieldEntry entry : modelClassDocVO.getFieldEntryList()) {
			StringBuffer buf = new StringBuffer();
			buf.append('\t').append("@ApiModelProperty(").append('"').append(entry.getFExplain()).append('"').append(')');
			writer.write(buf.toString());
			buf.setLength(0);
			if(entry.getFType().equals("byte")) {
				continue;
			}
			Class<?> clazz = Class.forName(entry.getFType());
			System.out.println(entry.getFType());
			buf.append('\t').append("private ").append(clazz.getSimpleName()).append(' ').append(entry.getFName()).append(';');
			writer.write(buf.toString());
			writer.writeEmptyLine();
		}

		if(suffix.equals("QueryParam")) {
			writer.write("\t@ApiModelProperty(\"开始时间\")");
			writer.write("\tprivate Long createStartTime;");
			writer.writeEmptyLine();
			writer.write("\t@ApiModelProperty(\"结束时间\")");
			writer.write("\tprivate Long createEndTime;");
		}
	}

	private void writeImport(ModelClassDocVO modelClassDocVO) throws IOException {
		writerBaseImport();
		if(null != modelClassDocVO.getFieldEntryList()) {
			for(FieldEntry entry : modelClassDocVO.getFieldEntryList()) {
				String typeName = entry.getFType();
				if(typeName.startsWith("java.lang") || typeName.startsWith("byte")) {
					continue;
				}else {
					writer.writeImport(entry.getFType());
				}
			}
		}
		writer.writeEmptyLine();
		
		writer.write("/**");
		writer.write(" *");
		writer.write(" * @author zhengbiaoxie");
		writer.write(" *");
		writer.write(" */");
	}

	private void writerBaseImport() throws IOException {
		writer.writePackage(voPkg);
		writer.writeEmptyLine();
		if(parentClassName.equals(endParentClass.getName())) {
			writer.writeImport(parentClassName);
		}
		writer.writeEmptyLine();
		writer.writeImport(ApiModel.class.getName());
		writer.writeImport(ApiModelProperty.class.getName());
		writer.writeImport(Data.class.getName());
		writer.writeImport(EqualsAndHashCode.class.getName());
		writer.writeImport(NoArgsConstructor.class.getName());
		writer.writeEmptyLine();
	}
}
