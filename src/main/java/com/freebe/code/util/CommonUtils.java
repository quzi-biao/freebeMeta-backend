package com.freebe.code.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.util.StringUtils;

/**
 * @author xiezhengbiao
 * @version Jul 6, 2020
 *
 */
public class CommonUtils {
    private static final char[] CHARS = new char[] {'0', '1', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    
    private static final Random RANDOM = new Random();
    
    /**
     * 随机字符串
     * @param length
     * @return
     */
    public static String randomString(int length) {
        StringBuffer b = new StringBuffer();
        for(int i = 0; i < length; i++) {
            b.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }
        
        return b.toString();
    }
    
      /**
     * 对比，如果为数值进行数值对比
     * @param s1
     * @param s2
     * @return
     */
    public static int compare(String s1, String s2) {
        try {
            float f1 = Float.parseFloat(s1);
            float f2 = Float.parseFloat(s2);
            return Math.round(f1 - f2);
        } catch (NumberFormatException e) {
            return s1.compareTo(s2);
        }
        
    }
    
    /**
     * set 转字符串
     * @param input
     * @return
     */
    @SuppressWarnings("deprecation")
	public static String set2Str(Set<String> input){
        if(StringUtils.isEmpty(input)) {
            return null;
        }
        StringBuffer b = new StringBuffer();
        for(String s : input) {
            b.append(s).append(',');
        }
        if(b.length() > 0) {
            b.setLength(b.length() - 1);
        }
        
        return b.toString();
    }
    
    /**
     * 
     * @param input
     * @return
     */
    @SuppressWarnings("deprecation")
	public static Set<String> str2Set(String input){
        if(StringUtils.isEmpty(input)) {
            return null;
        }
        String[] spilts = input.split(",");
        Set<String> ret = new HashSet<>();
        
        for(String s : spilts) {
            s = s.trim();
            if(!StringUtils.isEmpty(s)) {
                ret.add(s);
            }
        }
        
        return ret;
    }

    
    /**
     * 计算字符串的 MD5
     * @param input
     * @return
     */
    public static String md5(String input) {
        final int maxLength = 32;
        final String preffex = "0";
        byte[] retBytes = null;
        
        try {
            retBytes = MessageDigest.getInstance("md5").digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        
        String md5code = new BigInteger(1, retBytes).toString(16);
        for (int i = 0; i < maxLength - md5code.length(); i++) {
            md5code = preffex + md5code;
        }
        return md5code;
    }
    
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
        
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        
        return out.toByteArray();
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static Date formatDate(Date date) {
        if(null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置为东八区
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        sdf.format(date);
        
        return date;
    }
    
    /**
     * 
     * @return
     * @throws UnknownHostException
     */
    @SuppressWarnings("rawtypes")
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}
