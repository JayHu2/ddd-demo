package com.hero.ddd.domain.hrmanage.employee.event;

// 员工已入职 领域事件
public class EmployeeOnboarded {

  private String employeeCode;

  public EmployeeOnboarded(String employeeCode) {
    this.employeeCode = employeeCode;
  }

  // Getters and Setters
}
