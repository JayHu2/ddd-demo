package com.hero.ddd.domain.hrmanage;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hero.ddd.configaration.exceptions.BizCodes;
import com.hero.ddd.configaration.exceptions.BizException;
import com.hero.ddd.domain.hrmanage.context.BankCardDTO;
import com.hero.ddd.domain.hrmanage.employee.BankCard;
import com.hero.ddd.domain.hrmanage.employee.Employee;
import com.hero.ddd.domain.hrmanage.employee.event.BankCardAdded;
import com.hero.ddd.domain.hrmanage.employee.repository.EmployeeRepository;
import com.hero.ddd.infrastructure.utils.JavaBean;

/**
 * 添加银行卡应用服务
 * 一般会建议用业务维度拆分
 */
@Service
public class AddBankCardService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public void addBankCard(String employeeCode, BankCardDTO bankCardDTO) {

    Optional<Employee> employeeOptional = this.employeeRepository.findEmployee(employeeCode);

    if (!employeeOptional.isPresent()) {
      throw new BizException(BizCodes.EMPLOYEE_CODE_NOTEXIST, "员工编号不存在");
    }
    Employee employee = employeeOptional.get();
    employee.addBankCard(JavaBean.copyFrom(bankCardDTO, BankCard::new));
    this.employeeRepository.save(employee);

    // 发送领域事件
    sendEvent(new BankCardAdded(employee.getCode()));
  }

  void sendEvent(BankCardAdded event) {
    // 根据实际业务场景（看谁用）发送 应用内消息 或 中间件消息
  }
}
