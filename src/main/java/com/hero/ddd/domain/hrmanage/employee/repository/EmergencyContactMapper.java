package com.hero.ddd.domain.hrmanage.employee.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hero.ddd.domain.hrmanage.employee.data.EmergencyContactDO;

@Mapper
interface EmergencyContactMapper {

  public List<EmergencyContactDO> findEmergencyContactsByEmpployeeCode(String employeeCode);
}
