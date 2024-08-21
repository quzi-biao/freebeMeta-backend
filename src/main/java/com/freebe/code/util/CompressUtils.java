package com.freebe.code.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩工具
 * @author xiezhengbiao
 *
 */
public class CompressUtils {
	 /**
     * gzip 压缩
     * @param str
     * @return
     */
    public static byte[] gzipCompress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    
    /**
     * gzip 解压缩
     * @param bytes
     * @return
     */
    public static byte[] gzipUncompress(byte[] bytes) throws Exception{
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        
        GZIPInputStream ungzip;
		try {
			ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
	        int n;
	        while ((n = ungzip.read(buffer)) >= 0) {
	            out.write(buffer, 0, n);
	        }
	        
	        in.close();
	        byte[] ret = out.toByteArray();
	        out.close();
	        return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return bytes;
    }
}
