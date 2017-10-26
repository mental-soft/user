package com.teammental.user.service;

import com.teammental.memapper.MeMapper;
import com.teammental.user.constants.UserConstants;
import com.teammental.user.dto.UserDto;
import com.teammental.user.entity.User;
import com.teammental.user.exception.UserException;
import com.teammental.user.jpa.UserRepostory;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
  @Autowired
  private UserRepostory userRepostory;

  /**
   * Bütün user bilgilerinin geri gönderildiği metoddur.
   *
   * @return UserDto tipindi arrayList döner.
   */
  @Override
  public List<UserDto> getAll() {
    List<User> all = userRepostory.findAll();
    Optional<List<UserDto>> optional = MeMapper.getMapperFromList(all).mapToList(UserDto.class);
    if (CollectionUtils.isEmpty(optional.get())) {
      throw new UserException(0, "Herhangi bir kullanıcı bulunamadı.");
    }
    return optional.get();
  }

  /**
   * Bir user bilgisini user'a ait id alanından sorgulanarak getiren metoddur.
   *
   * @param userId kullanıcının unique id bilgisidir.
   * @return ID bilgisi iletilen kullanıcı bilgilerini döner.
   */
  @Override
  public UserDto getById(int userId) {
    try {
      User one = userRepostory.getOne(userId);
      Optional<UserDto> optional = MeMapper.getMapperFrom(one).mapTo(UserDto.class);
      if (!optional.isPresent()) {
        throw new UserException(0, UserConstants.NOT_FOUND);
      }
      return optional.get();
    } catch (Exception e) {
      LOGGER.error("id: " + userId + " olan kayıt çekilirken hata oluştu.", e);
      throw e;
    }
  }

  /**
   * Yeni bir kullanıcı oluşturmak için kullanılan metoddur.
   *
   * @param userDto Yeni oluşturulacak olan kullanıcıya ait bilgileri içiren UserDto bilgisini alır.
   * @return Kayıt edilen yeni user'a ait ID bilgisini geri döner.
   */
  @Override
  public int saveOrUpdate(UserDto userDto) {
    if (StringUtils.isEmpty(userDto.getName()) || StringUtils.isEmpty(userDto.getSurName())) {
      throw new UserException(1, UserConstants.NAME_SURNAME_REQUIRED);
    }
    if (StringUtils.isEmpty(userDto.getEmail())
        && StringUtils.isEmpty(userDto.getMobilePhone())) {
      throw new UserException(2, UserConstants.MAIL_MOBILEPHONE_REQUIRED);
    }
    if (isExistUser(userDto) == 1) {
      throw new UserException(3, UserConstants.SAME_MAIL);
    }
    if (isExistUser(userDto) == 2) {
      throw new UserException(4, UserConstants.SAME_USERNAME);
    }
    if (isExistUser(userDto) == 3) {
      throw new UserException(5, UserConstants.SAME_MOBILE_PHONE);
    }
    Optional<User> optional = MeMapper.getMapperFrom(userDto).mapTo(User.class);
    User user = userRepostory.save(optional.get());
    return user.getId();
  }

  /**
   * Pasif olan bir user bilgisini aktif etmek için kullanılan metoddur.
   *
   * @param userId Aktif edilecek user bilgisine ait ID bilgisidir.
   * @return Aktif edilmiş user id bilgisini döner.
   */
  @Override
  public int activatedUser(int userId) {
    UserDto userDto = getById(userId);
    if (userDto.getId() == null) {
      throw new UserException(0, "Herhangi bir kullanıcı bulunamadı.");
    }
    userDto.setActive(true);
    return saveOrUpdate(userDto);
  }

  /**
   * Aktif olan bir user bilgisinin pasif etmek için kullanılan metoddur.
   *
   * @param userId Pasif edilecek user bilgisine ait ID bilgisidir.
   * @return Pasif edilmiş user bilgisini döner.
   */
  @Override
  public int inActivatedUser(int userId) {
    UserDto userDto = getById(userId);
    if (userDto.getId() == null) {
      throw new UserException(0, "Herhangi bir kullanıcı bulunamadı.");
    }
    userDto.setActive(false);
    return saveOrUpdate(userDto);
  }


  /**
   * Kullanıcı adı, mail ve cep telefonu bilgileri tekil olması gerekir.
   * Bu metod ile aynı kayıtlar oluşmasının kontrolünü  yapıyoruz.
   *
   * @param dto Bu objede yer alan username, mail, cep telefonu alanları dolu gelir.
   * @return 0 Benzer kayıt yok, 1 email aynı kayıt mevcut, kullanıcı adı aynı kayıt mevcut,
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
    return CollectionUtils.isEmpty(liste)
        || liste.size() == 1 && liste.get(0).getId() == userDto.getId();
  }
}
