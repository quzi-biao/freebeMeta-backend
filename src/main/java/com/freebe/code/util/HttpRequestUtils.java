package com.freebe.code.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequestUtils {

	public static String httpGet(String urlStr, Map<String, String> headers) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);

            if(null != headers && headers.size() > 0) {
            	for(String key : headers.keySet()) {
            		connection.addRequestProperty(key, headers.get(key));
            	}
            }
            
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                byte[] data = readStream(is);
                return new String(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }
        return null;
	}
	
	public static String httpPost(String urlStr, String body, Map<String, String> headers) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            
            if(null != headers && headers.size() > 0) {
            	for(String key : headers.keySet()) {
            		connection.addRequestProperty(key, headers.get(key));
            	}
            }
            
            if(null != body) {
            	OutputStream out = connection.getOutputStream();
            	out.write(body.getBytes());
            	out.close();
            }
            
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                byte[] data = readStream(is);
                return new String(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }
        return null;
	}
	
	
	
	public static byte[] readStream(InputStream in) throws Exception {
		byte[] ret = null;
		int BUF_L = 4096;
		byte[] buf = new byte[BUF_L];
		int l = 0;
		while((l = in.read(buf)) > 0) {
			if(ret == null) {
				ret = new byte[l];
				System.arraycopy(buf, 0, ret, 0, l);
			}else {
				int length = ret.length + l;
				byte[] tmp = new byte[length];
				System.arraycopy(ret, 0, tmp, 0, ret.length);
				System.arraycopy(buf, 0, tmp, ret.length, l);
				ret = tmp;
			}
		}
		in.close();
		return ret;
	}
}
