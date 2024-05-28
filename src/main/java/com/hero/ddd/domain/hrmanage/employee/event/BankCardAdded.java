package com.hero.ddd.domain.hrmanage.employee.event;

// 银行卡新增 领域事件
public class BankCardAdded {

  private String employeeCode;

  public BankCardAdded(String employeeCode) {
    this.employeeCode = employeeCode;
  }

  // Getters and Setters
}
