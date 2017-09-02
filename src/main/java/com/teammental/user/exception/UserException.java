package com.teammental.user.exception;

/**
 * Coded busines exception'ların yönetildiği sınıftır.
 * Created by hcguler on 9/2/2017.
 */
public class UserException extends RuntimeException {

  private int code;
  private String label;

  /**
   * Throw ederken hızlı oluşturulması için eklenmiştir.
   */
  public UserException(int code, String label) {
    this.code = code;
    this.label = label;
  }

  /**
   * Code alanı ile dönülen hata mesajına göre rest uyarısı oluşuturulabilir.
   */
  public int getCode() {
    return code;
  }

  /**
   * Uyarı veya hata mesajının içeriğidir.
   */
  public String getLabel() {
    return label;
  }
}
