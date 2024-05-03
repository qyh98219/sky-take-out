package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.json.JacksonObjectMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.IUserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 15:36
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/user")
@Tag(name = "用户接口")
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result wxLogin(@RequestBody UserLoginDTO userLoginDTO) throws JsonProcessingException {
        String loginPath = "https://api.weixin.qq.com/sns/jscode2session";
        String reqPath = loginPath + "?appid=" + weChatProperties.getAppid() + "&secret=" + weChatProperties.getSecret() + "&js_code=" + userLoginDTO.getCode() + "&grant_type=authorization_code";
        String respValue = restTemplate.getForObject(reqPath, String.class);

        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();
        UserLoginVO userLoginVO = jacksonObjectMapper.readValue(respValue, UserLoginVO.class);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, userLoginVO.getOpenid());
        User user = userService.getOne(queryWrapper);
        //新用户
        if (Objects.isNull(user)){
            user = new User();
            user.setOpenid(userLoginVO.getOpenid());
            userService.save(user);
        }
        Map<String,Object> claims = new HashMap<>(2);
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        userLoginVO.setId(user.getId());
        userLoginVO.setToken(token);
        return Result.success(userLoginVO);
    }
}
