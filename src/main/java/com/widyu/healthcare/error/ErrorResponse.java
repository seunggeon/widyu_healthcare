package com.widyu.healthcare.error;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
  private boolean success;
  private T message;
  private long code;
}
