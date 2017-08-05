package com.teammental.user.dto;

/**
 * Bu sınıf user verilerinin arayüze aktarılması için kullanılacaktır.
 * User bilgisine ait ad, soyad, kullanıcı adı, telefon, cep telefonu, email ve aktiflik durumu bilgilerini içerir.
 * Created by hcguler on 8/5/2017.
 */
public class UserDto {

  private Integer id;
  private String userName;
  private String name;
  private String surName;
  private String phone;
  private String mobilePhone;
  private String email;
  private boolean isActive;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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
