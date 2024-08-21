package com.freebe.code.util.code.generator.doclet;

import lombok.Data;

/**
 * 属性字段对应注释
 *
 * @author guzt
 */
@Data
public class FieldEntry {

    /**
     * 参数名
     */
    private String fName;
    /**
     * 类型
     */
    private String fType;
    /**
     * 说明
     */
    private String fExplain;

    public FieldEntry(String fName, String fType, String fExplain) {
        super();
        this.fName = fName;
        this.fType = fType;
        this.fExplain = fExplain;
    }
}
