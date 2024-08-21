package com.freebe;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.freebe.code.GeneralApplication;
import com.freebe.code.business.base.service.impl.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={GeneralApplication.class, EmailServiceTest.class})
public class EmailServiceTest {

    @Resource
    private EmailService emailService;

    @Test
    public void test(){
        emailService.sendHtmlMail("450695496@qq.com","测试","测试");
    }
}
