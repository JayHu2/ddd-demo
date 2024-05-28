package com.hero.ddd.domain.hrmanage.employee.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hero.ddd.domain.hrmanage.employee.data.BankCardDO;

@Repository
public class BankCardRepository {

  @Autowired
  private BankCardMapper bankCardMapper;

  public List<BankCardDO> findBankCardsByEmpployeeCode(String employeeCode) {
    return bankCardMapper.findBankCardsByEmpployeeCode(employeeCode);
  }

}
