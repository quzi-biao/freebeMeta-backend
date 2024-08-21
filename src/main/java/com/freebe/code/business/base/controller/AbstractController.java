package com.freebe.code.business.base.controller;



import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.FileUtils;

import lombok.Cleanup;

/**
 * 
 * @author xiezhengbiao
 *
 */
public class AbstractController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取当前登录的用户
	 * @return
	 * @throws CustomException
	 */
	protected UserVO getCurrentUser() throws CustomException {
        try {
            Long id = Long.parseLong((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return userService.getUser(id);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
	
	/**
	 * 获取当前登录的ID
	 * @return
	 * @throws CustomException
	 */
	protected Long getCurrentUserID() throws CustomException {
        try {
            Long id = Long.parseLong((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return  id;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
	
	
	/**
	 * 获取用户 ID
	 * @return
	 * @throws CustomException
	 */
    protected void write(final HttpServletResponse response, String fileName, InputStream inputStream, String mimeType) throws CustomException {
        try {
            if (StringUtils.isEmpty(mimeType)) {
                mimeType = FileUtils.getMimeType(fileName);
                if (mimeType == null) {
                    mimeType = FileUtils.DEFAULT_MIME_TYPE;
                }
            }
            response.setContentType(mimeType);
            response.setCharacterEncoding("utf-8");

            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("filename", fileName);
            response.setHeader("Access-Control-Expose-Headers", "filename");

            if (!mimeType.contains("image") && !mimeType.contains("text")) {
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            }


            if (mimeType.contains("text")) {
                writeText(inputStream, response.getWriter());
            } else {
                FileUtils.copy(inputStream, response.getOutputStream());
            }


        } catch (Exception e) {
            throw new CustomException("输出文件流异常", e);
        }
    }

    protected void writeText(InputStream source, PrintWriter printWriter) throws IOException {
        @Cleanup InputStream ins = source;
        @Cleanup PrintWriter writer = printWriter;

        BufferedReader reader = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        writer.write(sb.toString());
    }

    /**
     * 设置 可通过postman 或者浏览器直接浏览
     *
     * @param response      response
     * @param bufferedImage bufferedImage
     * @throws Exception e
     */
    public void responseImage(HttpServletResponse response, BufferedImage bufferedImage) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
        ImageIO.write(bufferedImage, "jpeg", imageOutput);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        OutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
    }

}
