package com.teammental.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * User tablosu mappingi.
 * Created by hcguler on 7/22/2017.
 */
@Entity
@Table(name = "MENTAL_USER")
public class User {
  @Id
  @SequenceGenerator(name = "pk_sequence", sequenceName = "user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
  @Column(name = "ID", columnDefinition = "NUMERIC", unique = true, nullable = false)
  private Integer id;

  @Size(max = 50)
  @Column(name = "USER_NAME", columnDefinition = "VARCHAR")
  private String userName;

  @Size(max = 150)
  @Column(name = "NAME", columnDefinition = "VARCHAR")
  private String name;

  @Size(max = 150)
  @Column(name = "SURNAME", columnDefinition = "VARCHAR")
  private String surName;

  @Size(max = 50)
  @Column(name = "PHONE", columnDefinition = "VARCHAR")
  private String phone;

  @Size(max = 50)
  @Column(name = "MOBILE_PHONE", columnDefinition = "VARCHAR")
  private String mobilePhone;

  @Size(max = 250)
  @Column(name = "EMAIL", columnDefinition = "VARCHAR")
  private String email;

  @Column(name = "IS_ACTIVE", columnDefinition = "BOOLEAN")
  private boolean active;

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
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
