package com.hero.ddd.domain.hrmanage.employee.enums;

import java.util.NoSuchElementException;

public enum EmployeeStatus {

  Onboard((byte) 1);

  private final byte code;

  private EmployeeStatus(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static EmployeeStatus of(byte code) {

    for (EmployeeStatus s : EmployeeStatus.values()) {
      if (s.getCode() == code) {
        return s;
      }
    }
    throw new NoSuchElementException("不存在code[" + code + "]的员工状态");
  }
}
