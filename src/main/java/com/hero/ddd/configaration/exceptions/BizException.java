package com.hero.ddd.configaration.exceptions;

public class BizException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private long bizCode;

  public BizException(long bizCode, String message) {
    super(message);
    this.bizCode = bizCode;
  }
}
