package com.freebe.code.business.base.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;
	
    @Value("${spring.mail.username}")
    private String username;
    
    /**
     * 发送验证码
     * @param to
     * @param code
     */
    public void sendVerifyCode(String to, String code) {
    	String subject = "来自 FreeBe 社区的验证码";
    	StringBuffer buf = new StringBuffer();
    	buf.append("验证码:<span style='font-size:28px'>");
    	buf.append(code).append("</span>");
    	
    	this.sendHtmlMail(to, subject, buf.toString());
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    public  void sendHtmlMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件失败,收件人:{}", to, e);
        }
    }
}
