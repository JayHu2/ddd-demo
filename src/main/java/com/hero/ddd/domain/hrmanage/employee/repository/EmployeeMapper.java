package com.hero.ddd.domain.hrmanage.employee.repository;

import org.apache.ibatis.annotations.Mapper;

import com.hero.ddd.domain.hrmanage.employee.data.EmployeeDO;

@Mapper
interface EmployeeMapper {

  public EmployeeDO findEmployeeByCode(String code);
}
