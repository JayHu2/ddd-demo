package com.hero.ddd.domain.hrmanage.employee;

import java.util.Objects;

public class EmergencyContact {

  private String name;
  private String phoneNumber;
  private String email;

  public EmergencyContact() {
  }

  public EmergencyContact(String name, String phoneNumber, String email) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }

  // Getters and Setters

  @Override
  public int hashCode() {
    return Objects.hash(email, name, phoneNumber);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EmergencyContact other = (EmergencyContact) obj;
    return Objects.equals(email, other.email) && Objects.equals(name, other.name) && Objects.equals(phoneNumber, other.phoneNumber);
  }

}
