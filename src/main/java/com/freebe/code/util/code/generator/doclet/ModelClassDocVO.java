package com.freebe.code.util.code.generator.doclet;

import java.util.List;

import lombok.Data;

/**
 * model 类字段注释
 * @author guzt
 */
@Data
public class ModelClassDocVO {


    private String modelTableName;

    private String modelClassName;

    private String modelCommentText;

    private List<FieldEntry> fieldEntryList;

}
