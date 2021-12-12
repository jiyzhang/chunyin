package com.zhang.chunyin.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.chunyin.entity.WeixinUser;
import com.zhang.chunyin.repository.WeixinUserRepository;
import com.zhang.chunyin.rest.dto.SubmitParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author niuxianghui
 */
@Service
@Slf4j
public class ChunyinService {

    @Value("${chunyin.appId}")
    private String appId;

    @Value("${chunyin.secret}")
    private String secret;

    private final WeixinUserRepository weixinUserRepository;

    public ChunyinService(WeixinUserRepository weixinUserRepository) {
        this.weixinUserRepository = weixinUserRepository;
    }

    public void saveWeixinUser(SubmitParamDTO submitParamDTO) {
        if (submitParamDTO == null) {
            log.error("参数为空");
            return;
        }

        String openId = submitParamDTO.getOpenId();
        if (openId == null) {
            log.error("openId为空");
            return;
        }

        Optional<WeixinUser> weixinUserOptional = weixinUserRepository.findByOpenId(openId);
        WeixinUser weixinUser;
        weixinUser = weixinUserOptional.orElseGet(WeixinUser::new);
        weixinUser.setNickName(submitParamDTO.getNickName());
        weixinUser.setPhoneNumber(submitParamDTO.getPhoneNumber());
        weixinUser.setNumber(Long.valueOf(submitParamDTO.getOrderNumber()));
        weixinUser.setOpenId(submitParamDTO.getOpenId());
        weixinUserRepository.save(weixinUser);
    }

    public Optional<String> login(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" +
                appId +
                "&secret=" +
                secret +
                "&js_code=" +
                code.split(",")[0] +
                "&grant_type=authorization_code";
        String jsonString = HttpUtil.get(url);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Object openid = jsonObject.get("openid");
        if (openid instanceof String) {
            return Optional.of((String) openid);
        }
        return Optional.of(jsonString + url);
    }
}
