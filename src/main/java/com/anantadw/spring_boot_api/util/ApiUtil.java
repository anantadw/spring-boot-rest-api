package com.anantadw.spring_boot_api.util;

import org.springframework.http.HttpStatus;

import com.anantadw.spring_boot_api.dto.ApiResponse;

public class ApiUtil {
    public static ApiResponse buildApiResponse(String message, HttpStatus status, Object data) {
        ApiResponse response = new ApiResponse();
        response.setMessage(message);
        response.setStatusCode(status.value());
        response.setStatus(status.getReasonPhrase());
        response.setData(data);

        return response;
    }
}
