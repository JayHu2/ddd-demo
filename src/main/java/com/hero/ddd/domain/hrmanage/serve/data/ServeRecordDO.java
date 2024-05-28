package com.hero.ddd.domain.hrmanage.serve.data;

import java.time.LocalDate;

/**
 * 任职记录DO
 */
@SuppressWarnings("unused")
public class ServeRecordDO {

  private long id;

  private String employeeCode;

  private String orgCode;

  private String positionCode;
  
  private LocalDate startDate;
  
  private LocalDate endDate;

  private boolean primary;

  // Getters and Setters
}
