package com.hero.ddd.domain.hrmanage.employee.repository;

import java.util.List;

import com.hero.ddd.domain.hrmanage.employee.BankCard;
import com.hero.ddd.domain.hrmanage.employee.EmergencyContact;
import com.hero.ddd.domain.hrmanage.employee.Employee;
import com.hero.ddd.domain.hrmanage.employee.data.BankCardDO;
import com.hero.ddd.domain.hrmanage.employee.data.EmergencyContactDO;
import com.hero.ddd.domain.hrmanage.employee.data.EmployeeDO;
import com.hero.ddd.domain.hrmanage.employee.enums.EmployeeStatus;
import com.hero.ddd.infrastructure.utils.JavaBean;

public final class EmployeeFactory {

  public static Employee createEmployee(EmployeeDO employeeDO, List<BankCardDO> bankCardDOs, List<EmergencyContactDO> emergencyContactDOs) {

    // 对象拷贝
    List<BankCard> bankCards = JavaBean.copyFrom(bankCardDOs, BankCard::new);

    // 对象拷贝
    List<EmergencyContact> emergencyContacts = JavaBean.copyFrom(emergencyContactDOs, EmergencyContact::new);

    Employee employee = new Employee(employeeDO.getCode(), employeeDO.getName(), EmployeeStatus.of(employeeDO.getStatus()), bankCards, emergencyContacts);

    return employee;
  }
}
