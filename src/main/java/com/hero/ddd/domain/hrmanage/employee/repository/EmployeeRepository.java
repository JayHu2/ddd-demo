package com.hero.ddd.domain.hrmanage.employee.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hero.ddd.domain.hrmanage.employee.Employee;
import com.hero.ddd.domain.hrmanage.employee.data.BankCardDO;
import com.hero.ddd.domain.hrmanage.employee.data.EmergencyContactDO;
import com.hero.ddd.domain.hrmanage.employee.data.EmployeeDO;
import com.hero.ddd.domain.hrmanage.employee.enums.EmployeeStatus;

/**
 * 员工仓储
 */
@Repository
public class EmployeeRepository {

  @Autowired
  private EmployeeMapper employeeMapper;

  @Autowired
  private BankCardRepository bankCardRepository;

  @Autowired
  private EmergencyContactRepository emergencyContactRepository;

  public Optional<Employee> findEmployee(String employeeCode) {
    EmployeeDO employeeDO = this.employeeMapper.findEmployeeByCode(employeeCode);
    if (employeeDO == null) {
      return Optional.empty();
    }
    List<BankCardDO> bankCards = this.bankCardRepository.findBankCardsByEmpployeeCode(employeeDO.getCode());
    List<EmergencyContactDO> emergencyContacts = this.emergencyContactRepository.findEmergencyContactsByEmpployeeCode(employeeDO.getCode());
    return Optional.of(EmployeeFactory.createEmployee(employeeDO, bankCards, emergencyContacts));
  }

  public Employee createEmployee(String employeeCode, String name) {
    return new Employee(employeeCode, name, EmployeeStatus.Onboard, Collections.emptyList(), Collections.emptyList());
  }

  @Transactional
  public void save(Employee employee) {
    // 不要在这里做业务， Repository 只做 聚合&实体 的持久化

    // 有同学会问，难不成每一次都要完整地保存所有数据么？ 是的，因为Employee是一个聚合，员工是聚合根，维护了银行卡、紧急联系人等值对象， 所以必须要保证聚合内的完整性，
    //
    // 那么问题来了， 现实中有很多场景其实都只有小部分数据更新， 比如只新增一条银行卡信息， 更新全部岂不是开销很大？ 答案是是的，但是可以通过一些变更标记的方式来避免每次都全量更新

    // 保存员工实体
    // 保存银行卡信息
    // 保存紧急联系人信息
  }
}
