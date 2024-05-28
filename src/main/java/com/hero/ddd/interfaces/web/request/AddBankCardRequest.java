package com.hero.ddd.interfaces.web.request;

import java.time.LocalDate;

public class AddBankCardRequest {

  private String bankcode;
  private String bankName;
  private String cardNumber;
  private LocalDate validDate;
}
