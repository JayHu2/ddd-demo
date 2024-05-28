package com.hero.ddd.domain.hrmanage.employee.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hero.ddd.domain.hrmanage.employee.data.EmergencyContactDO;

@Repository
public class EmergencyContactRepository {

  @Autowired
  private EmergencyContactMapper emergencyContactMapper;

  public List<EmergencyContactDO> findEmergencyContactsByEmpployeeCode(String employeeCode) {
    return emergencyContactMapper.findEmergencyContactsByEmpployeeCode(employeeCode);
  }

}
