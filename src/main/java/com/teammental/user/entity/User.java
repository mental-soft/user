package com.teammental.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * User tablosu mappingi.
 * Created by hcguler on 7/22/2017.
 */
@Entity
@Table(name = "USER")
public class User {
  @Id
  @Column(name = "ID", columnDefinition = "NUMERIC")
  private int id;

  @Size(max = 50)
  @Column(name = "USER_NAME")
  private String userName;

  @Size(max = 150)
  @Column(name = "NAME")
  private String name;

  @Size(max = 150)
  @Column(name = "SURNAME")
  private String surName;

  @Size(max = 50)
  @Column(name = "PHONE")
  private String phone;

  @Size(max = 50)
  @Column(name = "MOBILE_PHONE")
  private String mobilePhone;

  @Size(max = 250)
  @Column(name = "EMAIL")
  private String email;

  @Column(name = "IS_ACTIVE")
  private boolean isActive;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurName() {
    return surName;
  }

  public void setSurName(String surName) {
    this.surName = surName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }
}
