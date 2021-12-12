package com.zhang.chunyin.rest.dto;

import lombok.Data;

/**
 * @author niuxianghui
 */
@Data
public class BasicResponseDTO<T> {

    private Boolean success;

    private Integer code;

    private String message;

    T data;

    public static <T> BasicResponseDTO<T> of(T data) {
        BasicResponseDTO<T> tBasicResponseDTO = new BasicResponseDTO<>();
        tBasicResponseDTO.setCode(200);
        tBasicResponseDTO.setMessage("success");
        tBasicResponseDTO.setSuccess(true);
        tBasicResponseDTO.setData(data);
        return tBasicResponseDTO;
    }

    public static <T> BasicResponseDTO<T> failure(String message) {
        BasicResponseDTO<T> tBasicResponseDTO = new BasicResponseDTO<>();
        tBasicResponseDTO.setCode(400);
        tBasicResponseDTO.setSuccess(false);
        tBasicResponseDTO.setMessage(message);
        return tBasicResponseDTO;
    }
}
