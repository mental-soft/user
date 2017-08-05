package com.teammental.user.controller;

import com.teammental.user.dto.UserDto;
import com.teammental.user.entity.User;
import com.teammental.user.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User bilgilerinin dışarıya açıldığı controller sınıfıdır.
 * Created by hcguler on 8/5/2017.
 */
@RestController
public class UserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
  @Autowired
  private UserService userService;

  /**
   * Tüm kullanıcı bilgilerini döner.
   * @return Bütün kullanıcılar json olarak döner.
   */
  @GetMapping(value = "/users", produces = "application/json")
  public ResponseEntity getAllUser() {
    List<UserDto> all = userService.getAll();
    //liste boş ise hata dön.
    if (CollectionUtils.isEmpty(all)){
      return ResponseEntity.badRequest().body("Herhangi bir kullanıcı bulunamadı.");
    }
    return ResponseEntity.ok().body(all);
  }

  /**
   * Yeni bir kullanıcı eklemek için kullanılır.
   * Parametre olarak json olarak user bilgileri gönderilir.
   */
  //todo id generate işleminde sorun var.
  @PostMapping(value = "/users", produces = "application/json")
  public ResponseEntity createUser(@RequestBody UserDto userDto) throws Exception {
    if (StringUtils.isEmpty(userDto.getName()) || StringUtils.isEmpty(userDto.getSurName())){
      return ResponseEntity.badRequest().body("Ad Soyad alanı doldurulması zorunludur.");
    }
    if (StringUtils.isEmpty(userDto.getEmail())){
      return ResponseEntity.badRequest().body("Mail adresi alanı doldurulması zorunludur.");
    }
    if (StringUtils.isEmpty(userDto.getMobilePhone())){
      return ResponseEntity.badRequest().body("Cep Telefonu alanı doldurulması zorunludur.");
    }
    if (userService.isExistUser(userDto) == 1){
      return  ResponseEntity.status(HttpStatus.CONFLICT).body("Bu mail adresi ile bir kullanıcı zaten var.");
    }
    if (userService.isExistUser(userDto) == 2){
      return  ResponseEntity.status(HttpStatus.CONFLICT).body("Bu kullanıcı adı ile bir kullanıcı zaten var.");
    }
    if (userService.isExistUser(userDto) == 3){
      return  ResponseEntity.status(HttpStatus.CONFLICT).body("Bu cep telefonu ile bir kullanıcı zaten var.");
    }
    userService.saveOrUpdate(userDto);
    return ResponseEntity.ok().body(userDto.getId());
  }

  /**
   * Kullanıcının url ile gönderdiği id bilgisine ait kullanıcı geri döner.
   * @param userId Bilgisi çekilecek user'a ait id bilgisidir.
   * @return id bilgisine ait user bilgilerini döner.
   * @throws Exception iş mantığına bağlı hata döner.
   */
  //todo exception handling çalışmıyor.
  @GetMapping(value = "/users/{userId}", produces = "application/json")
  public @ResponseBody ResponseEntity getUserById(@PathVariable Integer userId) throws Exception {
    try {
      UserDto userDto = userService.getById(userId);
      //liste boş ise hata dön.
      if (userDto.getId() == null){
        return ResponseEntity.badRequest().body("Herhangi bir kullanıcı bulunamadı.");
      }
      return ResponseEntity.ok().body(userDto);
    } catch (Exception e) {
      LOGGER.error("",e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

}
