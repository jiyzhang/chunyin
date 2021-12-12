package com.zhang.chunyin.rest;

import com.zhang.chunyin.entity.WeixinUser;
import com.zhang.chunyin.repository.WeixinUserRepository;
import com.zhang.chunyin.rest.dto.BasicResponseDTO;
import com.zhang.chunyin.rest.dto.SubmitParamDTO;
import com.zhang.chunyin.service.ChunyinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * @author niuxianghui
 */
@RestController
@RequestMapping("/chunyin")
@Slf4j
public class ChunyinController {

    private final ChunyinService chunyinService;

    private final WeixinUserRepository weixinUserRepository;

    @Value("${chunyin.basicPath}")
    private String basicPath;

    public ChunyinController(ChunyinService chunyinService, WeixinUserRepository weixinUserRepository) {
        this.chunyinService = chunyinService;
        this.weixinUserRepository = weixinUserRepository;
    }

    @GetMapping("/user")
    public BasicResponseDTO<WeixinUser> weixinUser(@RequestParam String openId) {
        Optional<WeixinUser> byOpenId = weixinUserRepository.findByOpenId(openId.split(",")[0]);
        return byOpenId.map(BasicResponseDTO::of).orElseGet(() -> BasicResponseDTO.failure("failure" + openId));
    }

    @GetMapping("/test2")
    public String testInter() {
        return "hello xianghui.";
    }

    @GetMapping("/login")
    public BasicResponseDTO<String> login(@RequestParam String code) {
        Optional<String> login = chunyinService.login(code);
        return login.map(BasicResponseDTO::of).orElseGet(() -> BasicResponseDTO.failure("获取openId失败."));
    }

    @PostMapping("/submit")
    public void submit(@RequestBody SubmitParamDTO submitParamDTO) {
        chunyinService.saveWeixinUser(submitParamDTO);
    }

    @PostMapping("/upload-image")
    public BasicResponseDTO<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String openId) {
        if (file.isEmpty()) {
            return BasicResponseDTO.failure("上传失败，请选择文件");
        }

        String fileName = file.getOriginalFilename();
        File dest = new File(basicPath + fileName);
        try {
            file.transferTo(dest);
            Optional<WeixinUser> weixinUserOptional = weixinUserRepository.findByOpenId(openId);
            if (weixinUserOptional.isPresent()) {
                WeixinUser weixinUser = weixinUserOptional.get();
                weixinUser.setImagePath(basicPath + fileName);
                weixinUserRepository.save(weixinUser);
                log.info("上传成功");
            } else {
                WeixinUser weixinUser = new WeixinUser();
                weixinUser.setOpenId(openId);
                weixinUser.setImagePath(basicPath + fileName);
                weixinUserRepository.save(weixinUser);
                log.info("上传成功");
            }
            return BasicResponseDTO.of("上传成功");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return BasicResponseDTO.failure("上传失败！");
    }

    @GetMapping("/image")
    public void getImage(HttpServletResponse response, @RequestParam String openId) {
        response.setContentType("image/jpeg");
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            Optional<WeixinUser> weixinUserOptional = weixinUserRepository.findByOpenId(openId);
            if (weixinUserOptional.isPresent()) {
                WeixinUser weixinUser = weixinUserOptional.get();
                String imagePath = weixinUser.getImagePath();
                File file = new File(imagePath);
                FileInputStream fileInputStream = new FileInputStream(file);
                IOUtils.copy(fileInputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
