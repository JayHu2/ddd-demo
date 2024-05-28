package com.hero.ddd.domain.hrmanage.employee;

import java.util.ArrayList;
import java.util.List;

import com.hero.ddd.domain.hrmanage.employee.enums.EmployeeStatus;
import com.hero.ddd.infrastructure.utils.Values;

/**
 * 员工聚合
 */
@SuppressWarnings("unused")
public class Employee {

  private String code; // 全局唯一标识符
  private String name;

  private EmployeeStatus status;

  private List<BankCard> bankCards;

  private List<EmergencyContact> emergencyContacts;

  public Employee(String code, String name, EmployeeStatus status, List<BankCard> bankCards,
      List<EmergencyContact> emergencyContacts) {
    this.code = code;
    this.name = name;
    this.status = status;
    this.bankCards = Values.orDefault(bankCards, ArrayList::new);
    this.emergencyContacts = Values.orDefault(emergencyContacts, ArrayList::new);
  }

  // Getters and Setters

  public void addBankCard(BankCard bankCard) {
    this.bankCards.add(bankCard);
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public EmployeeStatus getStatus() {
    return status;
  }

  public void updateBankCard(long oldId, BankCard newBankCard) {
    if (this.bankCards.removeIf(card -> card.getId() == oldId)) {
      throw new RuntimeException("更新银行卡信息失败，无法找到原银行卡信息");
    }
    this.bankCards.add(newBankCard);
  }
}
