package com.freebe.code.util.code.generator.generator;

import java.io.File;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.util.code.generator.CodeGenerator;
import com.freebe.code.util.code.generator.GeneratorUtils;
import com.freebe.code.util.code.generator.doclet.Doclet;
import com.freebe.code.util.code.generator.doclet.ModelClassDocVO;
import com.freebe.code.util.code.generator.doclet.SrcFileWriter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author xiezhengbiao
 *
 */
public class ControllerGenerator extends CodeGenerator {
	public static final String CONTROLLER_SUFFIX = "Controller";
	
	@Override
	public void generate() throws Exception {
		String fileName = GeneratorUtils.buildFileName(this.getSrcPath(), this.getCtrollerPkg(), this.getEntityName(), CONTROLLER_SUFFIX);
		if(new File(fileName).exists()) {
			System.out.println(CONTROLLER_SUFFIX + " 已存在：" + fileName);
			return;
		}
		
		System.out.println("创建文件：" + fileName);
		SrcFileWriter writer = new SrcFileWriter(fileName);
		
		String entryFilePath = GeneratorUtils.buildFileName(this.getSrcPath(), this.getEntityPkg(), this.getEntityName(), "");
		Doclet doclet = new Doclet(entryFilePath);
        ModelClassDocVO modelClassDocVO = doclet.exec();
        this.setEntityComment(modelClassDocVO.getModelCommentText());
        
		writer.writePackage(this.getCtrollerPkg());
		writer.writeEmptyLine();
		writerImport(writer);
		writerHeader(writer);
		writer.write("public class " + this.getEntityName() + "Controller {");
		
		writer.write("\t@Autowired");
		writer.write("\tprivate " + this.getEntityName() + "Service " + this.getEntityName().toLowerCase() + "Service;");
		writer.writeEmptyLine();
		
		writeUpdateMethod(writer);
		writeGetMethod(writer);
		writeQueryMethod(writer);
		writeDeleteMethod(writer);
		
		writer.write("}");
		
		writer.flush();
		writer.close();
	}

	private void writeDeleteMethod(SrcFileWriter w) throws IOException {
		w.write("\t@ApiOperation(value = \"删除" + this.getEntityComment() + "\")");
		w.write("\t@DeleteMapping(\"{id}\")");
		w.write("\tpublic ResultBean<?> delete(@PathVariable(\"id\") Long id) throws CustomException {");
		w.write("\t\t" + this.getEntityName().toLowerCase() + "Service.softDelete(id);");
		w.write("\t\treturn ResultBean.ok();");
		w.write("\t}");
		w.writeEmptyLine();
	}
	
	private void writeGetMethod(SrcFileWriter w) throws IOException {
		w.write("\t@ApiOperation(value = \"获取" + this.getEntityComment() + "\")");
		w.write("\t@GetMapping(\"get/{id}\")");
		w.write("\tpublic ResultBean<" + this.getEntityName() + "VO> get(@PathVariable(\"id\") Long id) throws CustomException {");
		w.write("\t\treturn ResultBean.ok(" + this.getEntityName().toLowerCase() + "Service.findById(id));");
		w.write("\t}");
		w.writeEmptyLine();
	}

	private void writeQueryMethod(SrcFileWriter w) throws IOException {
		w.write("\t@ApiOperation(value = \"查询" + this.getEntityComment() + "\")");
		w.write("\t@PostMapping(\"list\")");
		w.write("\tpublic ResultBean<Page<" + this.getEntityName() + "VO>> list(@Valid @RequestBody " + this.getEntityName() + "QueryParam param) throws CustomException {");
		w.write("\t\treturn ResultBean.ok(" + this.getEntityName().toLowerCase() + "Service.queryPage(param));");
		w.write("\t}");
		w.writeEmptyLine();
	}

	private void writeUpdateMethod(SrcFileWriter w) throws IOException {
		w.write("\t@ApiOperation(value = \"创建或者更新" + this.getEntityComment() + "\")");
		w.write("\t@PostMapping(\"createOrUpdate\")");
		w.write("\tpublic ResultBean<" + this.getEntityName() + "VO> createOrUpdate(@Valid @RequestBody " + this.getEntityName() + "Param param) throws CustomException {");
		w.write("\t\treturn ResultBean.ok(" + this.getEntityName().toLowerCase() + "Service.createOrUpdate(param));");
		w.write("\t}");
		w.writeEmptyLine();
	}

	private void writerHeader(SrcFileWriter w) throws IOException {
		w.write("@RestController");
		w.write("@RequestMapping(\"/" + this.getEntityName().toLowerCase() + "\")");
		w.write("@CrossOrigin(origins = \"*\")");
		w.write("@Api(tags=\"" + this.getEntityComment() + "接口\")");
	}

	private void writerImport(SrcFileWriter w) throws IOException {
		w.writeImport(Valid.class.getName());
		w.writeEmptyLine();
		
		w.writeImport(Autowired.class.getName());
		w.writeImport(Page.class.getName());
		w.writeImport(CrossOrigin.class.getName());
		w.writeImport(DeleteMapping.class.getName());
		w.writeImport(PathVariable.class.getName());
		w.writeImport(PostMapping.class.getName());
		w.writeImport(GetMapping.class.getName());
		w.writeImport(RequestBody.class.getName());
		w.writeImport(RequestMapping.class.getName());
		w.writeImport(RestController.class.getName());
		w.writeEmptyLine();
		
		w.writeImport(this.getServicePkg() + '.' + this.getEntityName() + "Service");
		w.writeImport(this.getVoPkg() + '.' + this.getEntityName() + "VO");
		w.writeImport(CustomException.class.getName());
		w.writeImport(ResultBean.class.getName());
		w.writeImport(this.getParamPkg() + '.' + this.getEntityName() + "Param");
		w.writeImport(this.getParamPkg() + '.' + this.getEntityName() + "QueryParam");
		w.writeEmptyLine();
		
		w.writeImport(Api.class.getName());
		w.writeImport(ApiOperation.class.getName());
		w.writeEmptyLine();

		w.write("/**");
		w.write(" *");
		w.write(" * @author zhengbiaoxie");
		w.write(" *");
		w.write(" */");
		
	}
}
