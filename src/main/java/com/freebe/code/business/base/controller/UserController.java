package com.freebe.code.business.base.controller;

import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.base.controller.param.LoginParam;
import com.freebe.code.business.base.controller.param.UpdatePasswordParam;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.meta.web3.AddressVerify;
import com.freebe.code.business.meta.web3.VerifyParam;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Api(tags = "用户模块")
public class UserController extends AbstractController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;
    
    @Autowired
    private AddressVerify addressVerify;

    HashMap<String,String> kaptchaCache = new HashMap<>();
    @ApiOperation(value = "获取当前用户详细信息", notes = "获取当前用户详细信息")
    @GetMapping("currentUserDetail")
    public ResultBean<?> currentUserDetail() throws CustomException {
        Object currentUser = super.getCurrentUser();
        return ResultBean.ok(currentUser);
    }

    @ApiOperation(value = "登录", notes = "用户登录")
    @PostMapping("login")
    public ResultBean<String> login(@Valid @RequestBody LoginParam loginParam) throws CustomException {
        String token = userService.login(loginParam);
        return ResultBean.ok("", token);
    }
    
    @ApiOperation(value = "注册", notes = "用户注册")
    @PostMapping("register")
    public ResultBean<String> register(@Valid @RequestBody LoginParam loginParam) throws CustomException {
        String token = userService.register(loginParam);
        return ResultBean.ok("", token);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("update/password")
    public ResultBean<?> updatePassword(@Valid @RequestBody UpdatePasswordParam param) throws CustomException {
        Long userId = userService.updatePassword(param);
        return ResultBean.ok(userId);
    }

    @ApiOperation(value = "登出")
    @GetMapping("logout")
    public ResultBean<?> logout(@RequestHeader("Authorization") String Authorization) throws CustomException {
    	Long userId = this.getCurrentUserID();
    	SecurityContextHolder.getContext().setAuthentication(null);
        //这边需要删除token
        userService.deleteAuthentication(Authorization);
        return ResultBean.ok(userId);
    }
    
    @ApiOperation(value = "获取验证码")
    @GetMapping("smsCode/{personId}")
    public ResultBean<String> getSmsCode(@PathVariable("personId") String personId) throws CustomException {
        return ResultBean.ok("", userService.getSmsByPersonId(personId));
    }


    @ApiOperation(value = "获取人机验证码")
    @GetMapping("/kaptcha")
    public ResultBean<?> getKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        // 生成验证码
        String uniqueId = RandomUtil.randomNumbers(6);
        String text = defaultKaptcha.createText();
        kaptchaCache.put(uniqueId,text);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(defaultKaptcha.createImage(text),"jpg",outputStream);
        String base64Img = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        // 创建验证码图片
        return ResultBean.ok(MapUtil.builder().put("captchaImg",base64Img).put("uniqueId",uniqueId).build());
    }

    @ApiOperation(value = "人机验证码校验")
    @GetMapping("/verifyKaptcha/{inputKaptcha}/{uniqueId}")
    public ResultBean<?> verifyKaptcha(HttpServletRequest request, @PathVariable("inputKaptcha")String inputKaptcha,@PathVariable("uniqueId")String uniqueId) {
        if(!kaptchaCache.containsKey(uniqueId) || !inputKaptcha.equalsIgnoreCase(kaptchaCache.get(uniqueId))){
            return ResultBean.error("验证码错误");
        }
        else {
            kaptchaCache.remove(uniqueId);
            return ResultBean.ok();
        }
    }
    
    @ApiOperation(value = "获取签名 Code")
  	@GetMapping("code/{address}")
  	public ResultBean<String> getSignCode(@PathVariable String address) throws CustomException {
  		return ResultBean.ok("", addressVerify.getVerifyCode(address));
  	}
  	
  	@ApiOperation(value = "签名校验")
  	@PostMapping("verify")
  	public ResultBean<String> verify(@RequestBody VerifyParam param) throws CustomException {
  		return ResultBean.ok("", addressVerify.auth(param));
  	}

}
