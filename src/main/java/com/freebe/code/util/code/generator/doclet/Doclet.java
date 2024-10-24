package com.freebe.code.util.code.generator.doclet;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

import cn.hutool.core.util.ReflectUtil;

public class Doclet {

    private static RootDoc rootDoc;
    private String javaBeanFilePath;

    public static boolean start(RootDoc root) {
        rootDoc = root;
        return true;
    }

    public Doclet(String javaBeanFilePath) {
        this.javaBeanFilePath = javaBeanFilePath;
    }

    public ModelClassDocVO exec() {
        ModelClassDocVO modelClassDocVO = new ModelClassDocVO();
        com.sun.tools.javadoc.Main.execute(new String[]{"-doclet", Doclet.class.getName(), "-docletpath",
                Doclet.class.getResource("/").getPath(), "-encoding", "utf-8", javaBeanFilePath});
        ClassDoc[] classes = rootDoc.classes();

        if (classes == null || classes.length == 0) {
            return modelClassDocVO;
        }

        List<FieldEntry> entrys = Lists.newArrayList();
        ClassDoc classDoc = classes[0];
        // 获取类的名称
        modelClassDocVO.setModelClassName(classDoc.name());
        // 获取类的注释
        String classComment = ReflectUtil.getFieldValue(classDoc, "documentation").toString();
        String spitStr = "\n";
        for (String msg : classComment.split(spitStr)) {
            if (!msg.trim().startsWith("@") && msg.trim().length() > 0) {
                modelClassDocVO.setModelCommentText(msg.trim());
                break;
            }
        }
        // 获取属性名称和注释
        FieldDoc[] fields = classDoc.fields(false);
        for (FieldDoc field : fields) {
            entrys.add(new FieldEntry(field.name(), field.type().qualifiedTypeName(), field.commentText().trim()));
        }

        modelClassDocVO.setFieldEntryList(entrys);
        return modelClassDocVO;
    }
}
