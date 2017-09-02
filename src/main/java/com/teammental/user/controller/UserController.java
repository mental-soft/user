package com.teammental.user.controller;

import com.teammental.user.dto.UserDto;
import com.teammental.user.exception.UserException;
import com.teammental.user.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User bilgilerinin dışarıya açıldığı controller sınıfıdır.
 * Created by hcguler on 8/5/2017.
 */
@RestController
public class UserController {
  public static final String USERS_MAPPING = "/users";
  public static final String USER_DETAIL_MAPPING = "/users/{userId}";
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
  @Autowired
  private UserService userService;

  /**
   * Tüm kullanıcı bilgilerini döner.
   *
   * @return Bütün kullanıcılar json olarak döner.
   */
  @GetMapping(value = USERS_MAPPING, produces = "application/json")
  public ResponseEntity getAllUser() {
    try {
      List<UserDto> all = userService.getAll();
      return ResponseEntity.ok().body(all);
    } catch (UserException e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body(e.getLabel());
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

  /**
   * Yeni bir kullanıcı eklemek için kullanılır.
   * Parametre olarak json olarak user bilgileri gönderilir.
   */
  @PostMapping(value = USERS_MAPPING, produces = "application/json")
  public ResponseEntity createUser(@RequestBody UserDto userDto) throws Exception {
    try {
      userService.saveOrUpdate(userDto);
      return ResponseEntity.ok().body(userDto.getId());
    } catch (UserException e) {
      LOGGER.error("", e);
      if (e.getCode() > 2 && e.getCode() < 6) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getLabel());
      }
      return ResponseEntity.badRequest().body(e.getLabel());
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

  /**
   * Kullanıcının url ile gönderdiği id bilgisine ait kullanıcı geri döner.
   *
   * @param userId Bilgisi çekilecek user'a ait id bilgisidir.
   * @return id bilgisine ait user bilgilerini döner.
   * @throws Exception iş mantığına bağlı hata döner.
   */
  @GetMapping(value = USER_DETAIL_MAPPING, produces = "application/json")
  public @ResponseBody ResponseEntity getUserById(@PathVariable Integer userId) throws Exception {
    try {
      UserDto userDto = userService.getById(userId);
      return ResponseEntity.ok().body(userDto);
    } catch (UserException e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body(e.getLabel());
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

  /**
   * Kayıtlı bir kullanıcı bilgisini güncellemek için kullanılır.
   *
   * @param userDto Güncellenecek kullanıcı bilgilerini parametre olarak alır.
   * @return Güncellenen kullanıcı bilgisini döner.
   * @throws Exception iş mantığına bağlı hata döner.
   */

  @PutMapping(value = USER_DETAIL_MAPPING, produces = "application/json",
      headers = "application/json")
  public @ResponseBody ResponseEntity updateUser(@RequestBody UserDto userDto) throws Exception {
    try {
      userService.saveOrUpdate(userDto);
      return ResponseEntity.ok().body("");
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

  /**
   * Var olan bir kullanıcının durumunu aktif yapmak için kullanılır.
   *
   * @param userId Aktif yapılacak kullanıcı bilgisinin ID'sini parametre alır.
   * @throws Exception iş mantığına bağlı hata döner.
   */
  @PutMapping(value = "/users/{userId}/activate", produces = "application/json")
  public @ResponseBody ResponseEntity activateUser(@PathVariable Integer userId) throws Exception {
    try {
      UserDto userDto = userService.getById(userId);
      if (userDto.getId() == null) {
        return ResponseEntity.badRequest().body("Herhangi bir kullanıcı bulunamadı.");
      }
      userService.activatedUser(userId);
      return ResponseEntity.ok().body("");
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

  /**
   * Var olan bir kullanıcıyı pasif yapmak için kullanılır.
   *
   * @param userId Pasif yapılacak kullanıcının ID'sini parametre alır.
   * @throws Exception iş mantığına bağlı hata döner.
   */
  @PutMapping(value = "/users/{userId}/pacify", produces = "application/json")
  public @ResponseBody ResponseEntity pacifyUser(@PathVariable Integer userId) throws Exception {
    try {
      UserDto userDto = userService.getById(userId);
      if (userDto.getId() == null) {
        return ResponseEntity.badRequest().body("Herhangi bir kullanıcı bulunamadı.");
      }
      userService.inActivatedUser(userId);
      return ResponseEntity.ok().body("");
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

}
