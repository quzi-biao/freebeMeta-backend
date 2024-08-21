package com.freebe.code.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.io.Resource;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import lombok.Cleanup;

@SuppressWarnings("unchecked")
public class FileUtils extends org.apache.commons.io.FileUtils{
	private static final int BUFFER_SIZE = 8192;
	
	public static final String DEFAULT_MIME_TYPE = "application/octet-stream";
	
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    public static String getMimeType(String fileName) {
        return getMimeType(MimeUtil.getMimeTypes(fileName));
    }
    
    public static String getMimeType(File file) {
        return getMimeType(MimeUtil.getMimeTypes(file));
    }
    
    public static String getMimeType(InputStream source) {
        return getMimeType((MimeUtil.getMimeTypes(new BufferedInputStream(source))));
    }
    
	public static String getMimeType(byte[] data) {
        return getMimeType((MimeUtil.getMimeTypes(data)));
    }
    
    public static String getMimeType(URL url) {
        return getMimeType(MimeUtil.getMimeTypes(url));
    }
    
    private static String getMimeType(Collection<MimeType> mimeTypes) {
        if(CollectionUtils.isNotEmpty(mimeTypes)) {
            Optional<MimeType> mimeTypeOpt = mimeTypes.stream().findFirst();
            if(mimeTypeOpt.isPresent()) {
                return mimeTypeOpt.get().toString();
            }
        }
        return DEFAULT_MIME_TYPE;
    }


	public static long copy(InputStream source, OutputStream sink)
            throws IOException {
        @Cleanup InputStream ins = source;
        @Cleanup OutputStream os = sink;
        long totalBytes = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = ins.read(buf)) > 0) {
            os.write(buf, 0, bytesRead);
            totalBytes += bytesRead;
        }
        return totalBytes;
    }

	
	/**
     * 将资源加载为一个字符串
     */
    public static String readResourceAsString(Resource resource) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStream in = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str ;
        while ((str = bufferedReader.readLine())!=null){
            builder.append(str);
        }
        //关闭流
        bufferedReader.close();
        inputStreamReader.close();
        in.close();
        return builder.toString();
    }
}
