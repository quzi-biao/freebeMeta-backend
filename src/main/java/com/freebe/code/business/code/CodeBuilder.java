package com.freebe.code.business.code;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端代码构建
 * @author zhengbiaoxie
 *
 */
public class CodeBuilder {
	public static final char S_TAG = '<';
	public static final char E_TAG = '>';
	public static final String ES_TAG = "</";
	public static final char SPACE = ' ';
	public static final char NL = '\n';
	public static final char EQUAL = '=';
	public static final char QUOTA = '\'';
	public static final char DQUOTA = '"';
	public static final char COLONS = ':';
	public static final char COMMA = ',';
	public static final char L_BRACKET = '{';
	public static final char R_BRACKET = '}';
	
	public static final String INTEND = "    ";
	public static final String INLINE_INTEND = "  ";
	public static final String STYLE_KEY = "style";
	
	/**
	 * 构建代码
	 * @param components
	 * @return
	 */
	public static List<String> buildCode(List<Component> components) {
		List<String> lines = new ArrayList<>();
		for(Component component : components) {
			List<String> subLine = buildCode(component);
			lines.addAll(subLine);
		}
		
		return lines;
	}
	
	public static List<String> buildCode(Component component) {
		List<String>  ret = new ArrayList<>();
		StringBuffer buf = new StringBuffer();
		
		buf.append(S_TAG).append(component.getElementTag());
		ret.add(buf.toString());
		int size = ret.size();
		addAttributeLines(component.getAttributes(), ret);
		addStyleLines(component.getStyles(), ret);
		int afterSize = ret.size();
		
		if(size == afterSize) {
			buf.append(E_TAG);
			ret.set(ret.size() - 1, buf.toString());
		}else {
			buf.setLength(0);
			buf.append(E_TAG);
			ret.add(buf.toString());
		}
		
		if(null != component.getContent()) {
			ret.add(component.getContent());
		}
		
		List<Component> subs = component.getComponents();
		
		if(null != subs && subs.size() > 0) {
			List<String> subLines = new ArrayList<>();
			for(Component sub : subs) {
				List<String> subLine = buildCode(sub);
				subLines.addAll(subLine);
			}
			for(int i = 0; i < subLines.size(); i++) {
				subLines.set(i, INTEND + subLines.get(i));
			}
			ret.addAll(subLines);
		}
		
		
		buf.setLength(0);
		buf.append(ES_TAG).append(component.getElementTag());
		buf.append(E_TAG);
		ret.add(buf.toString());
		
		return ret;
	}


	private static void addStyleLines(Styles styles, List<String> ret) {
		if(null == styles || styles.getValues().size() == 0) {
			return;
		}
		StringBuffer buf = new StringBuffer(INLINE_INTEND);
		buf.append(COLONS);
		buf.append(STYLE_KEY);
		buf.append(EQUAL);
		buf.append(DQUOTA);
		buf.append(L_BRACKET);
		ret.add(buf.toString());
		
		int index = 0;
		for(String name : styles.getValues().keySet()) {
			StringBuffer b = new StringBuffer(INLINE_INTEND).append(INLINE_INTEND);
			b.append(QUOTA).append(name).append(QUOTA);
			b.append(COLONS).append(SPACE);
			b.append(QUOTA).append(styles.getValues().get(name)).append(QUOTA);
			if(index < styles.getValues().size()) {
				b.append(COMMA);
			}
			ret.add(b.toString());
			index++;
		}
		buf.setLength(0);
		buf.append(INLINE_INTEND);
		buf.append(R_BRACKET);
		buf.append(DQUOTA);
		ret.add(buf.toString());
		
	}


	private static void addAttributeLines(List<ViewAttribute<?>> attributes, List<String> ret) {
		if(null == attributes || attributes.size() == 0) {
			return;
		}
		
		for(ViewAttribute<?> attribute : attributes) {
			StringBuffer buf = new StringBuffer(INLINE_INTEND);
			buf.append(attribute.getName());
			buf.append(EQUAL);
			buf.append(QUOTA);
			buf.append(attribute.getValue());
			buf.append(QUOTA);
			ret.add(buf.toString());
		}
	}
	
}
