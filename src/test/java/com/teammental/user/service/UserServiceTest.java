package com.teammental.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import com.teammental.memapper.MeMapper;
import com.teammental.user.dto.UserDto;
import com.teammental.user.entity.User;
import com.teammental.user.exception.UserException;
import com.teammental.user.jpa.UserRepostory;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.AssertTrue;
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
  public void findAllUser(){
    ArrayList<User> value = new ArrayList<>();
    value.add(getUser(1,"asda","asdad","asdad","asdad","asdad","asdad",true));
    when(userRepostory.findAll())
        .thenReturn(value);
    List<UserDto> all = userService.getAll();
    assertTrue(!CollectionUtils.isEmpty(all));
  }
  /*
  * Boş liste dönme durumu testi.
  */
  @Test(expected = UserException.class)
  public void findAllUserEmptyArray(){
    when(userRepostory.findAll())
        .thenReturn(new ArrayList<>());
    userService.getAll();
  }
  /*
  * Boş liste dönme durumu hata mesajı testi.
  */
  @Test
  public void findAllUserEmptyArrayExceptionCheck(){
    when(userRepostory.findAll())
        .thenReturn(new ArrayList<>());
    try {
      userService.getAll();
    } catch (UserException e) {
      assertEquals(e.getCode(),0);
    }
  }
  /*
  * İd ile kullanıcı çekme başarılı testi
  */
  @Test
  public void getByIdUserTest(){
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
  public void getByIdUserNoFoundTest(){
    when(userRepostory.getOne(1))
        .thenReturn(null);
    userService.getById(1);
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
