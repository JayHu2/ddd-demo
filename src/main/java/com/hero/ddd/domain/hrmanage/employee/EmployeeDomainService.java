package com.hero.ddd.domain.hrmanage.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hero.ddd.domain.hrmanage.employee.repository.EmployeeRepository;

/**
 * 员工领域服务
 */
@Service
public class EmployeeDomainService {

  @Autowired
  private EmployeeRepository employeeRepository;

  public boolean hasDuplicateCode(String employeeCode) {
    return this.employeeRepository.findEmployee(employeeCode).isPresent();
  }
}
