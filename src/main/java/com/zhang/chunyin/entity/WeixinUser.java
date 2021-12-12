package com.zhang.chunyin.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author niuxianghui
 */
@Entity
@Data
public class WeixinUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String openId;

    private String sessionKey;

    private String nickName;

    private Long number;

    private String imagePath;

    private String phoneNumber;
}
