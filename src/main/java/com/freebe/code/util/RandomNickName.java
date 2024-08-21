package com.freebe.code.util;

import java.util.Random;

public class RandomNickName {
	private static String[] PROJ_WORDS = new String[] {
			"善良", "豁达", "开朗", "仁慈", "和蔼",
			"直爽", "正直",
			"自信", "老实", "谦虚", "坚强", "顽强", "勇敢",
			"勤劳", "勤奋",
			"狂妄", "害羞", "羞涩", "羞答答"
	};
	
	private static String[] NAMES = new String[] {
			"金枪鱼", "鲨鱼", "章鱼", "鳄鱼",
			"老虎", "狮子", "猴子", "斑马", "狐狸", "大象", "犀牛", "长颈鹿", "穿山甲", "老狼",
			"猎豹", "花豹", "水牛", "羚羊",
			"秃鹫", "鹦鹉", "海鸥", "蜻蜓", "蝴蝶", "蜜蜂"
	};
	
	private static String[] AVATORS = new String[] {
			"06d84e285e280043cb12f07714049b84.jpeg",
			"07eaad688570ca527f341fc170665c7d.png",
			"086ca5630254a0a3329288cef1523cc.jpeg",
			"1c920214bb284b72fa1940d51e87e041.png",
			"1cfba21db419b94a6f8fd6f7ee70fbd6.png",
			"9d5d7d1cb2df013baa139b7d6bf42d20.jpg",
			"9397fd8f3f9f2cc5bd8cae74bd1dd9aa.jpg",
			"e6b0cf9d5c4b8af8bccf408ca4795a81.png"
	};
	
	private static Random r = new Random();
	
	public static String create() {
		int index = r.nextInt(PROJ_WORDS.length);
		StringBuffer buf = new StringBuffer();
		buf.append(PROJ_WORDS[index]);
		buf.append("的");
		int nameIndex = r.nextInt(NAMES.length);
		buf.append(NAMES[nameIndex]);
		
		return buf.toString();
	}

	public static String avator() {
		int index = r.nextInt(AVATORS.length);
		return AVATORS[index];
	}
}
