package com.hero.ddd.domain.hrmanage;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hero.ddd.configaration.exceptions.BizCodes;
import com.hero.ddd.configaration.exceptions.BizException;
import com.hero.ddd.domain.hrmanage.context.OnboardDTO;
import com.hero.ddd.domain.hrmanage.employee.Employee;
import com.hero.ddd.domain.hrmanage.employee.EmployeeDomainService;
import com.hero.ddd.domain.hrmanage.employee.event.EmployeeOnboarded;
import com.hero.ddd.domain.hrmanage.employee.repository.EmployeeRepository;
import com.hero.ddd.domain.hrmanage.organization.Organization;
import com.hero.ddd.domain.hrmanage.organization.repository.OrganizationRepository;
import com.hero.ddd.domain.hrmanage.position.Position;
import com.hero.ddd.domain.hrmanage.position.repository.PositionRepository;
import com.hero.ddd.domain.hrmanage.serve.ServeRecord;
import com.hero.ddd.domain.hrmanage.serve.repository.ServeRecordRepository;

/**
 * 入职应用服务
 * 一般会建议用业务维度拆分
 */
@Service
public class OnboardingService {

  @Autowired
  private EmployeeDomainService employeeDomainService;

  @Autowired
  private ServeRecordRepository serveRecordRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public void onboard(OnboardDTO onboardDTO) {
    if (this.employeeDomainService.hasDuplicateCode(onboardDTO.getEmployeeCode())) {
      throw new BizException(BizCodes.EMPLOYEE_CODE_DUPLICATED, "员工编号重复");
    }

    Optional<Organization> orgOptional = this.organizationRepository.findOrganization(onboardDTO.getOrgCode());
    if (!orgOptional.isPresent()) {
      throw new BizException(BizCodes.ORGANIZATION_CODE_NOTEXIST, "组织编号不存在");
    }
    Optional<Position> positionOptional = this.positionRepository.findPosition(onboardDTO.getPositionCode());
    if (!positionOptional.isPresent()) {
      throw new BizException(BizCodes.POSITION_CODE_NOTEXIST, "岗位编号不存在");
    }

    ServeRecord record = new ServeRecord();
    record.setEmployeeCode(onboardDTO.getEmployeeCode());
    record.setOrgCode(onboardDTO.getOrgCode());
    record.setPositionCode(onboardDTO.getPositionCode());
    record.setStartDate(onboardDTO.getEntryDate());

    this.serveRecordRepository.save(record);

    Employee employee = this.employeeRepository.createEmployee(onboardDTO.getEmployeeCode(), onboardDTO.getName());

    this.employeeRepository.save(employee);

    // 发送领域事件
    sendEvent(new EmployeeOnboarded(employee.getCode()));
  }

  void sendEvent(EmployeeOnboarded event) {
    // 根据实际业务场景（看谁用）发送 应用内消息 或 中间件消息
  }
}
