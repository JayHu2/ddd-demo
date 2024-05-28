package com.hero.ddd.domain.hrmanage.employee.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hero.ddd.domain.hrmanage.employee.data.BankCardDO;

@Mapper
interface BankCardMapper {

  public List<BankCardDO> findBankCardsByEmpployeeCode(String employeeCode);
}
