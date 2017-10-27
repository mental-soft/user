package com.teammental.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.teammental.memapper.MeMapper;
import com.teammental.user.dto.UserDto;
import com.teammental.user.entity.User;
import com.teammental.user.exception.UserException;
import com.teammental.user.jpa.UserRepostory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

/**
 * Created by hcguler on 10/21/2017.
 */
@SuppressWarnings({"PMD.TooManyStaticImports", "PMD.TooManyMethods"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
  @MockBean
  private UserRepostory userRepostory;

  @InjectMocks
  private UserService userService = new UserServiceImpl();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  /*
  * Tüm kullanıcıları getir metodunun başarılı test adımıdır.
  */
  @Test
  public void findAllUser() {
    ArrayList<User> value = new ArrayList<>();
    value.add(getUser(1, "asda", "asdad", "asdad", "asdad", "asdad", "asdad", true));
    when(userRepostory.findAll())
        .thenReturn(value);
    List<UserDto> all = userService.getAll();
    assertTrue(!CollectionUtils.isEmpty(all));
  }

  /*
  * Boş liste dönme durumu testi.
  */
  @Test(expected = UserException.class)
  public void findAllUserEmptyArray() {
    when(userRepostory.findAll())
        .thenReturn(new ArrayList<>());
    userService.getAll();
  }

  /*
  * Boş liste dönme durumu hata mesajı testi.
  */
  @Test
  public void findAllUserEmptyArrayExceptionCheck() {
    when(userRepostory.findAll())
        .thenReturn(new ArrayList<>());
    try {
      userService.getAll();
    } catch (UserException e) {
      assertEquals(e.getCode(), 0);
    }
  }

  /*
  * İd ile kullanıcı çekme başarılı testi
  */
  @Test
  public void getByIdUserTest() {
    User user = getUser(1, "asda", "asdad", "asdad", "asdad", "asdad", "asdad", true);
    when(userRepostory.getOne(1))
        .thenReturn(user);
    UserDto userDto = userService.getById(1);
    assertNotEquals(userDto, null);
  }

  /*
  * İd ile kullanıcı çekme başarılı testi
  */
  @Test(expected = UserException.class)
  public void getByIdUserNoFoundTest() {
    when(userRepostory.getOne(1))
        .thenReturn(null);
    userService.getById(1);
  }

  /*
  * İd ile kullanıcı aktif etme hatalı test
  */
  @Test(expected = UserException.class)
  public void activatedUserFailTest() {
    when(userRepostory.getOne(1))
        .thenReturn(null);
    userService.activatedUser(1);
  }

  /*
  * İd ile kullanıcı pasif etme hatalı test
  */
  @Test(expected = UserException.class)
  public void inActivatedUserFailTest() {
    when(userRepostory.getOne(1))
        .thenReturn(null);
    userService.inActivatedUser(1);
  }

  /*
  * İd ile kullanıcı aktif etme hatalı test
  */
  @Test
  public void activatedUserSuccessTest() {
    User user = getUser(6, "asda", "asdad", "asdad", "asdad", "asdad", "asdad", true);
    doReturn(user).when(userRepostory).getOne(any());
    doReturn(user).when(userRepostory).save(any(User.class));
    int i = userService.activatedUser(1);
    assertEquals(i, 6);
  }

  /*
  * İd ile kullanıcı pasif etme hatalı test
  */
  @Test
  public void inActivatedUserSuccessTest() {
    User user = getUser(5, "asda", "asdad", "asdad", "asdad", "asdad", "asdad", true);
    doReturn(user).when(userRepostory).getOne(any());
    doReturn(user).when(userRepostory).save(any(User.class));
    int i = userService.inActivatedUser(1);
    assertEquals(i, 5);
  }

  /*
  * İd ile kullanıcı pasif etme hatalı test
  */
  @Test
  public void saveOrUpdateUser() {
    User user = getUser(5, "asda", "asdad", "asdad", "asdad", "asdad", "asdad", true);
    doReturn(user).when(userRepostory).save(any(User.class));
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    int i = userService.saveOrUpdate((UserDto) optional.get());
    assertEquals(i, 5);
  }

  /*
  * bir kullanıcı kayıt ederken aynı mailli farklı kullanıcı var olması durumu testi.
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserConflictMail() {
    User user = getUser(5, "asda", "asdad", "asdad", "asdad", "asdad", "email", true);
    ArrayList<User> userArrayList = new ArrayList<>();
    userArrayList.add(getUser(2, "asda", "asdad", "asdad", "asdad", "asdad", "email", true));
    doReturn(userArrayList).when(userRepostory).findByEmail("email");
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /*
  * bir kullanıcı kayıt ederken aynı cep telefonlu farklı kullanıcı var olması durumu testi.
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserConflictMobilePhone() {
    User user = getUser(5, "asda", "asdad", "asdad", "asdad", "mobilePhone", "email", true);
    ArrayList<User> userArrayList = new ArrayList<>();
    userArrayList.add(getUser(2, "asda", "asdad", "asdad", "asdad", "asdad", "email", true));
    doReturn(userArrayList).when(userRepostory).findByMobilePhone("mobilePhone");
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /*
  * bir kullanıcı kayıt ederken aynı username farklı kullanıcı var olması durumu testi.
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserConflictUsername() {
    User user = getUser(5, "userName", "asdad", "asdad", "asdad", "mobilePhone", "email", true);
    ArrayList<User> userArrayList = new ArrayList<>();
    userArrayList.add(getUser(2, "asda", "asdad", "asdad", "asdad", "asdad", "email", true));
    doReturn(userArrayList).when(userRepostory).findByUserName("userName");
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /*
  * adsız kayıt atılmaya çalışılıyor
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserNameBos() {
    User user = getUser(5, "asdad", null, "asdad", "asdad", "mobilePhone", "email", true);
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /*
  * soyadsız kayıt atılmaya çalışılıyor
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserSurNameBos() {
    User user = getUser(5, "asdad", "asdasd", null, "asdad", "mobilePhone", "email", true);
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /*
  * cep telefonsuz kayıt atılmaya çalışılıyor
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserMobilePhoneBos() {
    User user = getUser(5, "asdad", "asdasd", "asdad", "asdad", null, "email", true);
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /*
  * cep telefonsuz kayıt atılmaya çalışılıyor
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserMailBos() {
    User user = getUser(5, "asdad", "asdasd", "asdad", "asdad", "asda", null, true);
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }
  /*
  * cep telefonsuz kayıt atılmaya çalışılıyor
  */
  @Test(expected = UserException.class)
  public void saveOrUpdateUserUserNameBos() {
    User user = getUser(5, null, "asdasd", "asdad", "asdad", "asda", "asda", true);
    Optional optional = MeMapper.getMapperFrom(user).mapTo(UserDto.class);
    userService.saveOrUpdate((UserDto) optional.get());
  }

  /**
   * User üretilmesi için yazılmıştır.
   */
  public User getUser(Integer id, String userName, String name, String surName,
                      String phone, String mobilePhone, String email, boolean active) {
    User user = new User();
    user.setId(id);
    user.setSurName(surName);
    user.setUserName(userName);
    user.setName(name);
    user.setPhone(phone);
    user.setMobilePhone(mobilePhone);
    user.setEmail(email);
    user.setActive(active);
    return user;
  }
}
