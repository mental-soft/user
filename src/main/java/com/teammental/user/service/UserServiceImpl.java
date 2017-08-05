package com.teammental.user.service;

import com.teammental.memapper.MeMapper;
import com.teammental.user.controller.UserController;
import com.teammental.user.dto.UserDto;
import com.teammental.user.entity.User;
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
    return optional.get();
  }

  /**
   * Bir user bilgisini user'a ait id alanından sorgulanarak getiren metoddur.
   *
   * @param userId kullanıcının unique id bilgisidir.
   * @return ID bilgisi iletilen kullanıcı bilgilerini döner.
   * @throws Exception iş mantığı kapsamında oluşan hataları döner.
   */
  @Override
  public UserDto getById(int userId) throws Exception {
    try {
      User one = userRepostory.getOne(userId);
      Optional<UserDto> optional = MeMapper.getMapperFrom(one).mapTo(UserDto.class);
      return optional.get();
    } catch (Exception e) {
      LOGGER.error("",e);
      return null;
    }
  }

  /**
   * ID bilgisi ile gelinen user bilgisini silmek için kullanılan metoddur.
   *
   * @param userId Silinecek user bilgisine ait id bilgisidir.
   */
  @Override
  public void deleteById(int userId) {
    userRepostory.delete(userId);
  }

  /**
   * Yeni bir kullanıcı oluşturmak için kullanılan metoddur.
   *
   * @param dto Yeni oluşturulacak olan kullanıcıya ait bilgileri içiren UserDto bilgisini alır.
   * @return Kayıt edilen yeni user'a ait ID bilgisini geri döner.
   * @throws Exception iş mantığı kapsamında oluşan hataları döner.
   */
  @Override
  public int saveOrUpdate(UserDto dto) throws Exception {
    Optional<User> optional = MeMapper.getMapperFrom(dto).mapTo(User.class);
    User user = userRepostory.save(optional.get());
    return (int) user.getId();
  }

  /**
   * Pasif olan bir user bilgisini aktif etmek için kullanılan metoddur.
   *
   * @param userId Aktif edilecek user bilgisine ait ID bilgisidir.
   * @return Aktif edilmiş user id bilgisini döner.
   * @throws Exception iş mantığı kapsamında oluşan hataları döner.
   */
  @Override
  public int activatedUser(int userId) throws Exception {
    UserDto userDto = getById(userId);
    userDto.setActive(true);
    return saveOrUpdate(userDto);
  }

  /**
   * Aktif olan bir user bilgisinin pasif etmek için kullanılan metoddur.
   *
   * @param userId Pasif edilecek user bilgisine ait ID bilgisidir.
   * @return Pasif edilmiş user bilgisini döner.
   * @throws Exception iş mantığı kapsamında oluşan hataları döner.
   */
  @Override
  public int inActivatedUser(int userId) throws Exception {
    UserDto userDto = getById(userId);
    userDto.setActive(false);
    return saveOrUpdate(userDto);
  }


  /**
   * Kullanıcı adı, mail ve cep telefonu bilgileri tekil olması gerekir.
   * Bu metod ile aynı kayıtlar oluşmasının kontrolünü  yapıyoruz.
   * @param dto Bu objede yer alan username, mail, cep telefonu alanları dolu gelir.
   * @return 0 Benzer kayıt yok
   *         1 email aynı kayıt mevcut
   *         2 kullanıcı adı aynı kayıt mevcut
   *         3 cep telefonu  aynı kayıt mevcut
   * @throws Exception iş mantığı kapsamında hataları döner.
   */
  @Override
  public int isExistUser(UserDto dto) throws Exception {
    int existChecker = 0;
    if (!StringUtils.isEmpty(dto.getUserName()) && !CollectionUtils.isEmpty(userRepostory.findByEmail(dto.getEmail())))
      existChecker = 1;
    if (!StringUtils.isEmpty(dto.getEmail()) && !CollectionUtils.isEmpty(userRepostory.findByUserName(dto.getUserName())))
      existChecker = 2;
    if (!StringUtils.isEmpty(dto.getMobilePhone()) && !CollectionUtils.isEmpty(userRepostory.findByMobilePhone(dto.getMobilePhone())))
      existChecker = 3;
    return existChecker;
  }
}
