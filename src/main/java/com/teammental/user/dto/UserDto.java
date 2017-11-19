package com.teammental.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 * Bu sınıf user verilerinin arayüze aktarılması için kullanılacaktır.
 * User bilgisine ait ad, soyad, kullanıcı adı, telefon, cep telefonu, email ve aktiflik durumu
 * bilgilerini içerir.
 * Created by hcguler on 8/5/2017.
 */
public class UserDto extends BaseDto<Integer> {

  @NotNull
  @Size(max = 200)
  private String userName;
  @Size(max = 200)
  private String name;
  @Size(max = 200)
  private String surName;
  @Size(max = 100)
  private String phone;
  @Size(max = 100)
  private String mobilePhone;
  @NotNull
  @Size(max = 400)
  @Email
  private String email;
  private boolean active;

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
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
