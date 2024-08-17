package com.anantadw.spring_boot_api.util;

import org.springframework.http.HttpStatus;

import com.anantadw.spring_boot_api.dto.ApiResponse;

public final class ApiUtil {
    public static ApiResponse buildApiResponse(
            String message,
            HttpStatus status,
            Object data,
            Object errors,
            Long total) {
        ApiResponse response = new ApiResponse();
        response.setMessage(message);
        response.setStatusCode(status.value());
        response.setStatus(status.getReasonPhrase());
        response.setData(data);
        response.setErrors(errors);
        response.setTotal(total);

        return response;
    }
}
