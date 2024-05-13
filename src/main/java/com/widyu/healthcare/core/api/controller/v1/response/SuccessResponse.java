package com.widyu.healthcare.core.api.controller.v1.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private boolean success;
    private T message;
    private T data;
}