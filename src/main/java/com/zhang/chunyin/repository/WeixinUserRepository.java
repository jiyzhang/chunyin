package com.zhang.chunyin.repository;

import com.zhang.chunyin.entity.WeixinUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author niuxianghui
 */
@Repository
public interface WeixinUserRepository extends JpaRepository<WeixinUser, Long> {

    /**
     * find by openId
     * @param openId openId
     * @return op
     */
    Optional<WeixinUser> findByOpenId(String openId);
}
