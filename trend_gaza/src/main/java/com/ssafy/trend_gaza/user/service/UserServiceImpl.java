package com.ssafy.trend_gaza.user.service;


import com.ssafy.trend_gaza.user.dto.ChangePwdRequest;
import com.ssafy.trend_gaza.user.dto.FindIdRequest;
import com.ssafy.trend_gaza.user.dto.FindPwdRequest;
import com.ssafy.trend_gaza.user.dto.LoginRequest;
import com.ssafy.trend_gaza.user.dto.ModifyProfileImageRequest;
import com.ssafy.trend_gaza.user.dto.ModifyRequest;
import com.ssafy.trend_gaza.user.dto.RegisterRequest;
import com.ssafy.trend_gaza.user.entity.User;
import com.ssafy.trend_gaza.user.exception.AccountAlreadyExist;
import com.ssafy.trend_gaza.user.repository.UserMapper;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final EmailService emailService;

    public UserServiceImpl(UserMapper userMapper, EmailService emailService) {
        super();
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @Override
    public void register(RegisterRequest registerRequest) throws Exception {
        userMapper.register(registerRequest);
        ;
    }

    @Override
    public User register(String nickname, String email, String socialId, String platform) {
        // User newUser = new User (nickname, email, socialId, platform);
        RegisterRequest newUser =
                RegisterRequest.builder()
                        .userId(socialId)
                        .userName(nickname)
                        // platform 넣기
                        .emailId(email.split("@")[0])
                        .emailDomain(email.split("@")[1])
                        .build();

        try {
            userMapper.register(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return User.builder()
                .userId(socialId)
                .userName(nickname)
                // platform 넣기
                .emailId(email.split("@")[0])
                .emailDomain(email.split("@")[1])
                .build();
    }

    @Override
    public int idCheck(String userId) throws Exception {
        return userMapper.idCheck(userId);
    }

    @Override
    public User login(LoginRequest loginRequest) throws Exception {
        return userMapper.login(loginRequest);
    }

    public User login(String platform, User user) {
        if (!platform.equals(user.getPlatform().getPlatform())) throw AccountAlreadyExist.EXCEPTION;

        return user;
    }

    @Override
    public User userInfo(String userId) throws Exception {
        return userMapper.userInfo(userId);
    }

    @Override
    public void saveRefreshToken(String userId, String refreshToken) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("token", refreshToken);
        userMapper.saveRefreshToken(map);
    }

    @Override
    public Object getRefreshToken(String userid) throws Exception {
        return userMapper.getRefreshToken(userid);
    }

    @Override
    public void deleteRefreshToken(String userId) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("token", null);
        userMapper.deleteRefreshToken(map);
    }

    @Override
    public String findId(FindIdRequest findIdRequest) throws Exception {
        return userMapper.findId(findIdRequest);
    }

    @Override
    public String findPwd(FindPwdRequest findPwdRequest) throws Exception {
        return userMapper.findPwd(findPwdRequest);
    }

    @Override
    public User view(String userId) throws Exception {
        return userMapper.view(userId);
    }

    @Override
    public void modify(ModifyRequest modifyRequest) throws Exception {
        userMapper.modify(modifyRequest);
    }

    @Override
    public int changePwd(ChangePwdRequest changePwdRequest) throws Exception {
        return userMapper.changePwd(changePwdRequest);
    }

    @Override
    public void delete(String userId) throws Exception {
        userMapper.delete(userId);
    }

    @Override
    @Transactional
    public void sendEmail(String userId) throws Exception {
        User user = userMapper.view(userId);
        String emailId = user.getEmailId();
        String emailDomain = user.getEmailDomain();
        String email = emailId + emailDomain;

        if (emailId == null || emailDomain == null) {
            throw new Exception();
        }

        String tempPwd = UUID.randomUUID().toString().substring(0, 8);
        emailService.sendMail(email, "[ trend_gaza ] 임시 비밀번호 발급", tempPwd);

        Map<String, String> map = new HashMap<String, String>();
        map.put("tmpPassword", tempPwd);
        map.put("userId", userId);
        userMapper.changePwdToTempPwd(map);
    }

    @Override
    public void logout(String userId) throws Exception {
        userMapper.logout(userId);
    }

    @Override
    public int modifyProfileImage(
            String userId, ModifyProfileImageRequest modifyProfileImageRequest) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("imgUrl", modifyProfileImageRequest.getImgUrl());
        return userMapper.modifyProfileImage(params);
    }
}
