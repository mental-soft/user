package com.teammental.user.service;

import com.teammental.meconfig.bll.service.BaseCrudServiceImpl;
import com.teammental.meconfig.exception.entity.EntityInsertException;
import com.teammental.meconfig.exception.entity.EntityNotFoundException;
import com.teammental.meconfig.exception.entity.EntityUpdateException;
import com.teammental.user.constants.UserConstants;
import com.teammental.user.dto.UserDto;
import com.teammental.user.entity.User;
import com.teammental.user.exception.UserException;
import com.teammental.user.jpa.UserRepostory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * User sınıfının VT işlemleri ve iş mantıklarının tanımlandığı service'dir.
 * Created by hcguler on 8/5/2017.
 */
@Service
@Transactional
public class UserServiceImpl
    extends BaseCrudServiceImpl<UserDto, Integer>
    implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
  @Autowired
  private UserRepostory userRepostory;

  @Override
  protected JpaRepository getRepository() {
    return userRepostory;
  }

  @Override
  protected Class<?> getDtoClass() {
    return UserDto.class;
  }

  @Override
  protected Class<?> getEntityClass() {
    return User.class;
  }

  /**
   * Pasif olan bir user bilgisini aktif etmek için kullanılan metoddur.
   *
   * @param userId Aktif edilecek user bilgisine ait ID bilgisidir.
   * @return Aktif edilmiş user id bilgisini döner.
   */
  @Override
  public int activatedUser(Integer userId) {
    try {
      UserDto userDto = null;
      userDto = findById(userId);
      userDto.setActive(true);
      UserDto update = update(userDto);
      return update.getId();
    } catch (EntityNotFoundException e) {
      LOGGER.error("Kayıt Bulunamadı.");
      throw new UserException(0, UserConstants.NOT_FOUND);
    } catch (EntityUpdateException e) {
      LOGGER.error("Kayıt Güncellenirken hata oluştu.");
      throw new UserException(0, "Kayıt Güncellenirken hata oluştu.");
    }
  }

  /**
   * Aktif olan bir user bilgisinin pasif etmek için kullanılan metoddur.
   *
   * @param userId Pasif edilecek user bilgisine ait ID bilgisidir.
   * @return Pasif edilmiş user bilgisini döner.
   */
  @Override
  public int inActivatedUser(Integer userId) {
    try {
      UserDto userDto = null;
      userDto = findById(userId);
      userDto.setActive(false);
      UserDto update = update(userDto);
      return update.getId();
    } catch (EntityNotFoundException e) {
      LOGGER.error("Kayıt Bulunamadı.");
      throw new UserException(0, UserConstants.NOT_FOUND);
    } catch (EntityUpdateException e) {
      LOGGER.error("Kayıt Güncellenirken hata oluştu.");
      throw new UserException(0, "Kayıt Güncellenirken hata oluştu.");
    }
  }

  /**
   * Kullanıcıya ait email ve telefon numarası ile id bilgisini geri dönen metoddur.
   *
   * @param info email, telefon veya username
   * @return userId bilgisi döner.
   */
  @Override
  public Integer getUserId(String info) throws EntityNotFoundException {
    List<User> byUserName = userRepostory.findByUserName(info);
    List<User> byEmail = userRepostory.findByEmail(info);
    List<User> byMobilePhone = userRepostory.findByMobilePhone(info);
    if (!CollectionUtils.isEmpty(byUserName)) {
      return byUserName.get(0).getId();
    }
    if (!CollectionUtils.isEmpty(byEmail)) {
      return byEmail.get(0).getId();
    }
    if (!CollectionUtils.isEmpty(byMobilePhone)) {
      return byMobilePhone.get(0).getId();
    }
    throw new EntityNotFoundException("Kullanıcı bilgisi bulunamadı.");
  }

  /**
   * Yeni bir kullanıcı oluşturmak için kullanılan metoddur.
   *
   * @param dto Yeni oluşturulacak olan kullanıcıya ait bilgileri içiren UserDto bilgisini alır.
   * @return Kayıt edilen yeni user'a ait ID bilgisini geri döner.
   * @throws EntityInsertException Kayıt işlemi sırasında hata alınırsa döner.
   */
  @Override
  protected Integer doInsert(UserDto dto) throws EntityInsertException {
    kaliteKontrol(dto);
    return super.doInsert(dto);
  }

  /**
   * Bir kullanıcı kaydı güncellenirken kullanılır.
   *
   * @param dto Güncellenecek olan veri.
   * @return Güncellenen verinin güncellendikten sonraki son halini döner.
   * @throws EntityNotFoundException Eğer güncellenecek veri bulunamaz ise atar.
   * @throws EntityUpdateException   Eğer güncelleme sırasında sorun olursa atar.
   */
  @Override
  protected UserDto doUpdate(UserDto dto) throws EntityNotFoundException, EntityUpdateException {
    kaliteKontrol(dto);
    return super.doUpdate(dto);
  }

  private void kaliteKontrol(UserDto userDto) {
    if (StringUtils.isEmpty(userDto.getName()) || StringUtils.isEmpty(userDto.getSurName())) {
      throw new UserException(HttpStatus.BAD_REQUEST.value(), UserConstants.NAME_SURNAME_REQUIRED);
    }
    if (StringUtils.isEmpty(userDto.getEmail())
        || StringUtils.isEmpty(userDto.getMobilePhone())) {
      throw new UserException(HttpStatus.BAD_REQUEST.value(),
          UserConstants.MAIL_MOBILEPHONE_REQUIRED);
    }
    if (StringUtils.isEmpty(userDto.getUserName())) {
      throw new UserException(HttpStatus.BAD_REQUEST.value(),
          UserConstants.USERNAME_REQUIRED);
    }
    if (isExistUser(userDto) == 1) {
      throw new UserException(HttpStatus.CONFLICT.value(), UserConstants.SAME_USERNAME);
    }
    if (isExistUser(userDto) == 2) {
      throw new UserException(HttpStatus.CONFLICT.value(), UserConstants.SAME_MAIL);
    }
    if (isExistUser(userDto) == 3) {
      throw new UserException(HttpStatus.CONFLICT.value(), UserConstants.SAME_MOBILE_PHONE);
    }
  }


  /**
   * Kullanıcı adı, mail ve cep telefonu bilgileri tekil olması gerekir.
   * Bu metod ile aynı kayıtlar oluşmasının kontrolünü  yapıyoruz.
   *
   * @param dto Bu objede yer alan username, mail, cep telefonu alanları dolu gelir.
   * @return 0 Benzer kayıt yok,
   *         1 email aynı kayıt mevcut, kullanıcı adı aynı kayıt mevcut,
   *         3 cep telefonu  aynı kayıt mevcut
   * @throws Exception iş mantığı kapsamında hataları döner.
   */
  private int isExistUser(UserDto dto) {
    int existChecker = 0;
    if (!StringUtils.isEmpty(dto.getUserName())
        && !mukerrerKontrolu(userRepostory.findByUserName(dto.getUserName()), dto)) {
      existChecker = 1;
    }
    if (!StringUtils.isEmpty(dto.getEmail())
        && !mukerrerKontrolu(userRepostory.findByEmail(dto.getEmail()), dto)) {
      existChecker = 2;
    }
    if (!StringUtils.isEmpty(dto.getMobilePhone())
        && !mukerrerKontrolu(userRepostory.findByMobilePhone(dto.getMobilePhone()), dto)) {
      existChecker = 3;
    }
    return existChecker;
  }

  private boolean mukerrerKontrolu(List<User> liste, UserDto userDto) {
    //güncellenen nesne için id check yapılır.
    //yeni kayıt için veri tabanından bir kayıt varsa conflict olur.
    if (userDto != null && userDto.getId() != null) {
      return CollectionUtils.isEmpty(liste)
          || liste.size() == 1 && liste.get(0).getId().equals(userDto.getId());
    } else {
      return CollectionUtils.isEmpty(liste);
    }
  }
}
