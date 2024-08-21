package com.freebe.code.util.code.generator.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.entity.BaseEntity;
import com.freebe.code.business.base.entity.CodeEntity;
import com.freebe.code.business.base.entity.IdEntity;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.code.generator.CodeGenerator;
import com.freebe.code.util.code.generator.GeneratorUtils;
import com.freebe.code.util.code.generator.doclet.SrcFileWriter;

public class ServiceImplGenerator extends CodeGenerator {
	public static final String SERVICE_IMPL_SUFFIX = "ServiceImpl";
	
	private Class<?> entityClass = null;
	
	@Override
	public void generate() throws Exception {
		String entityClassName = this.getEntityPkg() + '.' + this.getEntityName();
		entityClass = Class.forName(entityClassName);
		
		String fileName = GeneratorUtils.buildFileName(this.getSrcPath(), this.getServiceImplPkg(), this.getEntityName(), SERVICE_IMPL_SUFFIX);
		if(new File(fileName).exists()) {
			System.out.println(SERVICE_IMPL_SUFFIX + " 已存在：" + fileName);
			return;
		}
		
		System.out.println("创建文件：" + fileName);
		SrcFileWriter writer = new SrcFileWriter(fileName);
		
		writer.writePackage(this.getServiceImplPkg());
		writer.writeEmptyLine();
		writerImport(writer);
		writer.write("@Service");
		
		StringBuffer classDefine = new StringBuffer("public class ");
		classDefine.append(this.getEntityName()).append(SERVICE_IMPL_SUFFIX).append(" ");
		classDefine.append("extends BaseServiceImpl<").append(this.getEntityName()).append("> ");
		classDefine.append("implements ").append(this.getEntityName()).append("Service {");
		
		writer.write(classDefine.toString());

		writer.write("\t@Autowired");
		writer.write("\tprivate " + this.getEntityName() + "Repository repository;");
		writer.writeEmptyLine();
		
		writer.write("\t@Autowired");
		writer.write("\tprivate ObjectCaches objectCaches;");
		writer.writeEmptyLine();
		
		writeGetMethod(writer);
		writeUpdateMethod(writer);
		writeQueryMethod(writer);
		writeSpecMethod(writer);
		writeCastVOMethod(writer);
		writeDeleteMethod(writer);
		
		writer.write("}");
		
		writer.flush();
		writer.close();
	}
	
	private void writeDeleteMethod(SrcFileWriter writer) throws IOException {
		writer.write("\t@Override");
		StringBuffer buf = new StringBuffer();
		buf.append("\tpublic void softDelete(Long id) throws CustomException {");
		writer.write(buf.toString());
		writer.write("\t\tobjectCaches.delete(id, " + getEntityName() + "VO.class);");
		writer.write("\t\tsuper.softDelete(id);");
		writer.write("\t}");
		writer.writeEmptyLine();
	}

	private void writeGetMethod(SrcFileWriter writer) throws IOException {
		writer.write("\t@Override");
		StringBuffer buf = new StringBuffer();
		buf.append("\tpublic ");
		buf.append(this.getEntityName()).append("VO findById(Long id) throws CustomException {");
		writer.write(buf.toString());
		writer.write("\t\t" + this.getEntityName() + "VO ret = this.objectCaches.get(id, " + this.getEntityName() + "VO.class);");
		writer.write("\t\tif(null == ret){");
		writer.write("\t\t\tOptional<" + this.getEntityName() + "> op = this.repository.findById(id);");
		writer.write("\t\t\tif(!op.isPresent()){");
		writer.write("\t\t\t\treturn null;");
		writer.write("\t\t\t}");
		writer.write("\t\t\tret = toVO(op.get());");
		writer.write("\t\t}");
		writer.write("\t\tobjectCaches.put(ret.getId(), ret);");
		writer.write("\t\treturn ret;");
		writer.write("\t}");
		writer.writeEmptyLine();
	}


	private void writeSpecMethod(SrcFileWriter writer) throws IOException {
		StringBuffer buf = new StringBuffer();
		buf.append("\tprivate ");
		buf.append("Specification<").append(this.getEntityName()).append("> buildSpec(").append(this.getEntityName())
			.append("QueryParam param) throws CustomException {");
		writer.write(buf.toString());
		
		writer.write("\t\treturn new Specification<" + this.getEntityName() + ">() {");
		
		writer.write("\t\t\tprivate static final long serialVersionUID = 1L;");
		writer.writeEmptyLine();
		writer.write("\t\t\t@Override");
		buf.setLength(0);
		buf.append("\t\t\t").append("public Predicate toPredicate(Root<").append(this.getEntityName()).append("> root, ");
		buf.append("CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {");
		writer.write(buf.toString());
		writer.write("\t\t\t\tQueryBuilder<" + this.getEntityName() + "> builder = new QueryBuilder<>(root, criteriaBuilder);");
		writer.write("\t\t\t\tbuilder.addEqual(\"isDelete\", false);");
		writer.writeEmptyLine();
		writer.write("\t\t\t\tbuilder.addBetween(\"createTime\", param.getCreateStartTime(), param.getCreateEndTime());");
		writer.write("\t\t\t\treturn query.where(builder.getPredicate()).getRestriction();");
		
		writer.write("\t\t\t}");
		writer.write("\t\t};");
		writer.write("\t}");
		writer.writeEmptyLine();
	}

	private void writeCastVOMethod(SrcFileWriter writer) throws IOException {
		StringBuffer buf = new StringBuffer();
		buf.append("\tprivate ");
		buf.append(this.getEntityName()).append("VO ").append("toVO(").append(this.getEntityName()).append(" e) throws CustomException {");
		writer.write(buf.toString());
		writer.write("\t\t" + this.getEntityName() + "VO vo = new " + this.getEntityName() + "VO();");
		writer.write("\t\tvo.setId(e.getId());");
		writer.write("\t\tvo.setName(e.getName());");
		writer.write("\t\tvo.setCode(e.getCode());");
		writer.write("\t\tvo.setCreateTime(e.getCreateTime());");
		writer.writeEmptyLine();
		
		writeVoFieldSetting(writer, entityClass);
		
		writer.writeEmptyLine();
		writer.write("\t\treturn vo;");
		writer.write("\t}");
		writer.writeEmptyLine();
	}

	private void writeQueryMethod(SrcFileWriter writer) throws IOException {
		writer.write("\t@Override");
		StringBuffer buf = new StringBuffer();
		buf.append("\tpublic ");
		buf.append("Page<").append(this.getEntityName()).append("VO> queryPage(").append(this.getEntityName()).append("QueryParam").append(" param) throws CustomException {");
		writer.write(buf.toString());
		writer.write("\t\tparam.setOrder(\"id\");");
		writer.write("\t\tPageRequest request = PageUtils.toPageRequest(param);");
		writer.writeEmptyLine();
		writer.write("\t\tSpecification<" + this.getEntityName() + "> example = buildSpec(param);");
		writer.writeEmptyLine();
		writer.write("\t\tPage<" + this.getEntityName() + "> page = repository.findAll(example, request);");
		writer.write("\t\tList<" + this.getEntityName() + "VO> retList = new ArrayList<>();");
		writer.writeEmptyLine();
		writer.write("\t\tfor(" + this.getEntityName() + " e:  page.getContent()) {");
		writer.write("\t\t\tretList.add(toVO(e));");
		writer.write("\t\t}");
		writer.write("\t\treturn new PageImpl<" + this.getEntityName() + "VO>(retList, page.getPageable(), page.getTotalElements());");
		writer.write("\t}");
		writer.writeEmptyLine();
	}


	private void writeUpdateMethod(SrcFileWriter writer) throws IOException {
		writer.write("\t@Override");
		StringBuffer buf = new StringBuffer();
		buf.append("\tpublic ");
		buf.append(this.getEntityName()).append("VO createOrUpdate(").append(this.getEntityName()).append("Param").append(" param) throws CustomException {");
		writer.write(buf.toString());
		writer.write("\t\t" + this.getEntityName() + " e = this.getUpdateEntity(param);");
		writer.writeEmptyLine();
		
		writeEntityFieldSetting(writer, entityClass);
		
		writer.writeEmptyLine();
		writer.write("\t\te = repository.save(e);");
		writer.writeEmptyLine();
		writer.write("\t\t" + this.getEntityName() + "VO vo = toVO(e);");
		writer.write("\t\tobjectCaches.put(vo.getId(), vo);");
		writer.writeEmptyLine();
		writer.write("\t\treturn vo;");
		writer.write("\t}");
		writer.writeEmptyLine();
	}
	
	private void writeVoFieldSetting(SrcFileWriter writer, Class<?> clazz) throws IOException {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			String name = field.getName();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			writer.write("\t\tvo.set" + name + "(e.get" + name + "());");
		}
		
		Class<?> superClazz = clazz.getSuperclass();
		if(superClazz.equals(BaseEntity.class) || superClazz.equals(IdEntity.class) || superClazz.equals(Object.class) || superClazz.equals(CodeEntity.class)) {
			return;
		}else {
			writer.writeEmptyLine();
			this.writeVoFieldSetting(writer, superClazz);
		}
	}
	
	private void writeEntityFieldSetting(SrcFileWriter writer, Class<?> clazz) throws IOException {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			String name = field.getName();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			writer.write("\t\te.set" + name + "(param.get" + name + "());");
		}
		
		Class<?> superClazz = clazz.getSuperclass();
		if(superClazz.equals(BaseEntity.class) || superClazz.equals(IdEntity.class) || superClazz.equals(Object.class) || superClazz.equals(CodeEntity.class)) {
			return;
		}else {
			writer.writeEmptyLine();
			this.writeEntityFieldSetting(writer, superClazz);
		}
	}

	private void writerImport(SrcFileWriter writer) throws IOException {
		writer.writeEmptyLine();
		writer.writeImport(ArrayList.class.getName());
		writer.writeImport(List.class.getName());
		writer.writeImport(Optional.class.getName());
		writer.writeEmptyLine();
		writer.writeImport(CriteriaBuilder.class.getName());
		writer.writeImport(CriteriaQuery.class.getName());
		writer.writeImport(Predicate.class.getName());
		writer.writeImport(Root.class.getName());
		writer.writeEmptyLine();
		writer.writeImport(Autowired.class.getName());
		writer.writeImport(Page.class.getName());
		writer.writeImport(PageImpl.class.getName());
		writer.writeImport(PageRequest.class.getName());
		writer.writeImport(Specification.class.getName());
		writer.writeImport(Service.class.getName());
		writer.writeEmptyLine();
		writer.writeImport(ObjectCaches.class.getName());
		writer.writeImport(BaseServiceImpl.class.getName());
		writer.writeImport(CustomException.class.getName());
		writer.writeImport(PageUtils.class.getName());
		writer.writeImport("com.freebe.code.util.QueryUtils.QueryBuilder");
		writer.writeImport(this.getEntityPkg() + "." + this.getEntityName());
		writer.writeImport(this.getServicePkg() + "." + this.getEntityName() + "Service");
		writer.writeImport(this.getVoPkg() + "." + this.getEntityName() + "VO");
		writer.writeImport(this.getParamPkg() + "." + this.getEntityName() + "Param");
		writer.writeImport(this.getParamPkg() + "." + this.getEntityName() + "QueryParam");
		writer.writeImport(this.getRepositoryPkg()+ "." + this.getEntityName() + "Repository");
		writer.writeEmptyLine();
		
		writer.write("/**");
		writer.write(" *");
		writer.write(" * @author zhengbiaoxie");
		writer.write(" *");
		writer.write(" */");
	}

}
