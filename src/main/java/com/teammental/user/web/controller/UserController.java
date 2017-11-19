package com.teammental.user.web.controller;

import com.teammental.meconfig.exception.entity.EntityDeleteException;
import com.teammental.meconfig.exception.entity.EntityInsertException;
import com.teammental.meconfig.exception.entity.EntityNotFoundException;
import com.teammental.meconfig.exception.entity.EntityUpdateException;
import com.teammental.meconfig.web.controller.BaseCrudController;
import com.teammental.user.constants.UserConstants;
import com.teammental.user.dto.UserDto;
import com.teammental.user.exception.UserException;
import com.teammental.user.service.UserService;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User bilgilerinin dışarıya açıldığı controller sınıfıdır.
 * Created by hcguler on 8/5/2017.
 */
@RestController
@RequestMapping(UserConstants.BASE_URL)
public class UserController extends BaseCrudController<UserService, UserDto, Integer> {
  public static final String USER_DETAIL_MAPPING = "/users/{userId}";
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
  @Autowired
  private UserService userService;

  /**
   * UserException controller seviyesinde dönüştürülür.
   *
   * @param dto Kayıt edilecek dto
   * @return başarılı kayıt sonrasında id bilgisini döner.
   * @throws EntityInsertException Kayıt sırasında oluşan hatadır.
   */
  @Override
  protected Serializable doInsert(UserDto dto) throws EntityInsertException {
    try {
      return super.doInsert(dto);
    } catch (UserException e) {
      LOGGER.error("", e);
      throw new EntityInsertException(e.getLabel());
    }
  }

  /**
   * UserException controller seviyesinde dönüştürülür.
   *
   * @param dto Güncellenecek dto
   * @return başarılı güncelleme sonrasında id bilgisini döner.
   * @throws EntityInsertException Güncelleme sırasında oluşan hatadır.
   */
  @Override
  protected UserDto doUpdate(UserDto dto) throws EntityNotFoundException, EntityUpdateException {
    try {
      return super.doUpdate(dto);
    } catch (UserException e) {
      LOGGER.error("", e);
      throw new EntityUpdateException(e.getLabel());
    }
  }

  /**
   * Silme işlemi tanımı olmadığı için override edilerek hiçbir işlem yapılmamıştır.
   *
   * @param id silinecek userId bilgisi.
   * @return delete is not defigned for user table.
   * @throws EntityNotFoundException id ile gelinen kullanıcı bulunamaz ise atar.
   * @throws EntityDeleteException   kaydı silerken sorun yaşarsa atar.
   */
  @Override
  protected boolean doDelete(Integer id) throws EntityNotFoundException, EntityDeleteException {
    //do nothing for delete function
    return false;
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
      userService.inActivatedUser(userId);
      return ResponseEntity.ok().body("");
    } catch (Exception e) {
      LOGGER.error("", e);
      return ResponseEntity.badRequest().body("Hata oluştu.");
    }
  }

  @Override
  protected UserService getBaseCrudService() {
    return userService;
  }

  @Override
  protected String getMappingUrlOfController() {
    return UserConstants.BASE_URL;
  }
}
