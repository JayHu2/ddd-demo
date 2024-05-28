package com.hero.ddd.domain.hrmanage.employee;

import java.time.LocalDate;
import java.util.Objects;

public class BankCard {

  private long id;
  private String bankcode;
  private String bankName;
  private String cardNumber;
  private LocalDate validDate;

  public BankCard() {
  }

  // Getters and Setters

  @Override
  public int hashCode() {
    return Objects.hash(bankName, bankcode, cardNumber, id, validDate);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BankCard other = (BankCard) obj;
    return Objects.equals(bankName, other.bankName) && Objects.equals(bankcode, other.bankcode) && Objects.equals(cardNumber, other.cardNumber)
        && id == other.id && Objects.equals(validDate, other.validDate);
  }

}
