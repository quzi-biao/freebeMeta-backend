package com.freebe.code.business.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freebe.code.common.CustomException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AI 代码
 * @author zhengbiaoxie
 *
 */
public class OpenCoder {
	public static final String ENDPOINT = "https://api.chatanywhere.tech/v1/chat/completions";
	
	public static final String APIKEY = "sk-14798YNZFufC1UEngLB2xfP4YmqRupb5HXCrwnL3OWRXteg6";
	
	public static final String CODE_TAG = "```";
	
	public static void main(String[] args) throws CustomException {
		cssCode("画一只小狗");
	}
	
	public static String cssCode(String prompt) throws CustomException {
		OpenParam param = new OpenParam();
		param.setModel("gpt-3.5-turbo");
		param.addMessages("system", "You are a Front-end Developer.");
		param.addMessages("user", prompt);

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, JSONObject.toJSONString(param));
		Request request = new Request.Builder().url(ENDPOINT)
		   .method("POST", body)
		   .addHeader("Authorization", "Bearer " + APIKEY)
		   .addHeader("User-Agent", "Apifox/1.0.0")
		   .addHeader("Content-Type", "application/json")
		   .build();
		
		try {
			Response response = new OkHttpClient().newCall(request).execute();
			if(null == response || null == response.body()) {
				throw new CustomException("openapi 接口请求失败: 返回为空");
			}
			
			String retContent = getContent(response);
			System.out.println(retContent);
			return getCode(retContent);
		} catch (Exception e) {
			if(e instanceof CustomException) {
				throw (CustomException)e;
			}
			throw new CustomException(e);
		}
	}

	
	/**
	 * 提取代码
	 * @param retContent
	 * @return
	 * @throws CustomException
	 */
	private static String getCode(String retContent) throws CustomException {
		int sindex = retContent.indexOf(CODE_TAG);
		if(sindex < 0) {
			throw new CustomException("openapi 接口请: 无可用代码");
		}
		int eindex = retContent.indexOf(CODE_TAG, sindex + CODE_TAG.length());
		if(eindex < 0) {
			throw new CustomException("openapi 接口请: 代码块异常");
		}
		String code = retContent.substring(sindex + CODE_TAG.length(), eindex);
		System.out.println(code);
		return code;
	}

	/**
	 * 从 openapi 的返回结果中提取内容
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws CustomException
	 */
	private static String getContent(Response response) throws IOException, CustomException {
		String ret = new String(response.body().bytes());
		System.out.println(ret);
		if(null == ret || ret.length() == 0) {
			throw new CustomException("openapi 接口请求失败: 返回为空");
		}
		
		JSONObject jo = JSONObject.parseObject(ret);
		JSONArray choices = jo.getJSONArray("choices");
		if(null == choices || choices.size() == 0) {
			throw new CustomException("openapi 接口: 无可用返回内容");
		}
		JSONObject choice = choices.getJSONObject(0);
		JSONObject message = choice.getJSONObject("message");
		if(null == message) {
			throw new CustomException("openapi 接口: 无可用返回内容2");
		}
		String content = message.getString("content");
		if(null == content || content.length() == 0) {
			throw new CustomException("openapi 接口: 无可用返回内容3");
		}
		
		return content;
	}
	
	@Data
	public static class OpenParam {
		private String model;
		
		private int maxTokens = 256;
		
		private List<OpenParamMessage> messages = new ArrayList<>();
		
		public OpenParam addMessages(String role, String content) {
			this.messages.add(new OpenParamMessage(role, content));
			return this;
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class OpenParamMessage {
		private String role;
		
		private String content;
	}
}
