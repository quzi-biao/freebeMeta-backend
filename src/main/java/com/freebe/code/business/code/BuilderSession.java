package com.freebe.code.business.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.file.FileUtils;
import com.freebe.code.business.code.command.ActionEnum;
import com.freebe.code.business.code.command.ActionMatcher;
import com.freebe.code.business.code.command.EditCommand;
import com.freebe.code.common.CustomException;

import lombok.Data;

/**
 * 内容创建的上下文
 * @author zhengbiaoxie
 *
 */
@Data
@org.springframework.stereotype.Component
public class BuilderSession {
	/**
	 * 当前组件
	 */
	private Component attentionComponent;
	
	/**
	 * 页面
	 */
	private Vue2Page page;
	
	/**
	 * 页面路径
	 */
	private String pagePath = "/Users/zhengbiaoxie/Workspace/code-wander/src/views/Main.vue";
	
	/**
	 * 根组件
	 */
	private Component root = null;
	
	@PostConstruct
	public void init() throws Exception {
		page = new Vue2Page();
		
		Parser parser = Parser.htmlParser();
		parser.settings(new ParseSettings(true, true));
		Document doc = parser.parseInput(FileUtils.readFileToString(new File(pagePath), Charset.forName("UtF-8")), "");
		List<Node> nodes = doc.childNodes().get(0).childNodes();
		
		for(Node node : nodes) {
			List<Node> subNodes = node.childNodes();
			for(Node subNode : subNodes) {
				if(subNode instanceof TextNode) {
					continue;
				}
				Element ele = (Element)subNode;
				Component c = toComponent(ele);
				if(c.getElementTag().equals("template")) {
					page.setTemplate(c);
					this.root = page.getTemplate().getComponents().get(0);
					this.attentionComponent = root;
				}else if(c.getElementTag().equals("script")) {
					page.setScript(c);
				}else if(c.getElementTag().equals("style")) {
					page.setStyle(c);
				}
			}
		}
		
//		page.setTemplate(new Component("template"));
//		root = new Component("div");
//		root.getComponents().add(new AiCodeBar().build());
//		
//		page.getTemplate().getComponents().add(root);
//		page.setScript(new Component("script"));
//		page.getScript().setContent(buildScript());
//		page.setStyle(new Component("style"));
		
	}
	
	

	public void updatePage(String prompt) throws CustomException {
		System.out.println(prompt);
		if(null == prompt || prompt.length() == 0) {
			this.writeCode();
			return;
		}
		
		EditCommand command = ActionMatcher.match(prompt);
		System.out.println(command);
		if(command.getAction() == ActionEnum.CSS) {
			String cssCode = OpenCoder.cssCode("CSS代码:" + prompt);
			Map<String, String> cssMap = toCssMap(cssCode);
			attentionComponent.getStyles().getValues().putAll(cssMap);
		}else if(command.getAction() == ActionEnum.ADD) {
			if(null == command.getComponentBuilder()) {
				throw new CustomException("未识别的组件");
			}
			try {
				ComponentBuilder componentBuilder = command.getComponentBuilder().getConstructor().newInstance();
				List<Component> components = componentBuilder.build();
				this.attentionComponent.getComponents().addAll(components);
				this.attentionComponent = components.get(components.size() - 1);
			} catch (Exception e) {
				throw new CustomException("构建失败", e);
			}
			
		}
		
		this.writeCode();
	}
	
	/**
	 * 
	 * @param cssCode
	 * @return
	 */
	private Map<String, String> toCssMap(String cssCode) {
		String[] lines = cssCode.split("\n");
		Map<String, String>  ret = new HashMap<>();
		for(String line : lines) {
			int index = line.indexOf(CodeBuilder.COLONS);
			if(index <= 0) {
				continue;
			}
			String key = line.substring(0, index).trim();
			String value = line.substring(index + 1, line.length() - 1).trim();
			ret.put(key, value);
		}
		System.out.println(ret);
		return ret;
	}

	/**
	 * 写入代码
	 */
	private void writeCode() {
		List<String> codeLines = CodeBuilder.buildCode(Arrays.asList(page.getTemplate(), page.getScript(), page.getStyle()));
		
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(this.pagePath));
			for(String line : codeLines) {
				System.out.println(line);
				w.write(line);
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将页面元素解析成组件
	 * @param e
	 * @return
	 */
	private Component toComponent(Element e) {
		Component c = new Component(e.tag().getName());
		
		List<Attribute> atts = e.attributes().asList();
		for(Attribute att : atts) {
			if(att.getKey().equals(":style")) {
				String style = att.getValue();
				JSONObject jo = JSONObject.parseObject(style);
				for(String key : jo.keySet()) {
					c.getStyles().add(key, jo.getString(key));
				}
			}else {
				c.getAttributes().add(new ViewAttribute<String>(att.getKey(), att.getValue()));
			}
		}
		
		List<Node> nodes = e.childNodes();
		for(Node node : nodes) {
			if(node instanceof DataNode) {
				c.setContent(((DataNode)node).getWholeData());
			}
			
			if(node instanceof Element) {
				c.getComponents().add(toComponent((Element)node));
			}
			
			if(node instanceof TextNode) {
			}
		}
		return c;
	}
	
//	private String buildScript() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("import AiCode from './AiCode'\n");
//		buf.append('\n');
//		buf.append("export default {\n");
//		buf.append("  name: 'Main',\n");
//		buf.append("  components: {AiCode},\n");
//		buf.append("  data(){\n");
//		buf.append("    return {}\n");
//		buf.append("  },\n");
//		buf.append("  created(){\n");
//		buf.append("  },\n");
//		buf.append("  methods:{\n");
//		buf.append("  }\n");
//		buf.append("}");
//		
//		return buf.toString();
//	}
}
