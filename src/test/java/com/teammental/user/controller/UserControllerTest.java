package com.teammental.user.controller;

/**
 * Created by hcguler on 8/5/2017.
 */

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teammental.user.constants.UserConstants;
import com.teammental.user.dto.UserDto;
import com.teammental.user.exception.UserException;
import com.teammental.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Test classı.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTest.class);
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  List<UserDto> mockUserList;

  /**
   * Unit testler için kullanılacak değerler oluşturulur.
   */
  @Before
  public void init() throws Exception {
    mockUserList = new ArrayList<>();
    mockUserList.add(getUserDto(1,
        "asdad", "asd", "asdad",
        "asdaddsa", "asdad", "asdad", true));
    mockUserList.add(getUserDto(2,
        "asssdad", "asssd", "asdassd",
        "asdassddsa", "asdaggd", "asjjdad", false));
    mockUserList.add(getUserDto(3,
        "rrr", "y", "d",
        "rrt", "hfd", "asjjdad", false));
    mockUserList.add(getUserDto(4,
        "rrr", "y", "d",
        "rrt", "hfd", "asjjdad", false));
    Mockito.when(userService.saveOrUpdate(any(UserDto.class)))
        .thenAnswer(
            new Answer<Integer>() {
              @Override
              public Integer answer(InvocationOnMock invocation) throws Exception {
                Object[] arguments = invocation.getArguments();
                UserDto argument = (UserDto) arguments[0];
                if (2 == argument.getId()) {
                  throw new Exception();
                } else if (3 == argument.getId()) {
                  throw new UserException(HttpStatus.CONFLICT.value(), UserConstants.SAME_MAIL);
                } else if (4 == argument.getId()) {
                  throw new UserException(2, UserConstants.MAIL_MOBILEPHONE_REQUIRED);
                }
                return 1;
              }
            });
  }

  /**
   * Mocklanan değerler için dönüş alınan testtir.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test
  public void getAllUsers() throws Exception {
    Mockito.when(userService.getAll()).thenReturn(mockUserList);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * geri dönüş değeri yokken alınan mesaj test ediliyor.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test
  public void getAllUsersFail() throws Exception {
    Mockito.when(userService.getAll()).thenThrow(new UserException(0, UserConstants.NOT_FOUND));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(UserConstants.NOT_FOUND, result.getResponse().getContentAsString());
  }

  /**
   * Service katmanından exception yollandığında request test ediliyor.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test(expected = Exception.class)
  public void getAllUsersException() throws Exception {
    Mockito.when(userService.getAll()).thenThrow(new Exception("Hata"));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
  }

  /**
   * getbyId unit test.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test
  public void getUserById() throws Exception {
    Mockito.when(userService.getById(1)).thenReturn(mockUserList.get(0));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING + "/1")
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(200, result.getResponse().getStatus());
    ;
  }

  /**
   * getbyId unit test.
   */
  @Test
  public void getUserByIdFail() throws Exception {
    Mockito.when(userService.getById(1)).thenThrow(new UserException(0, UserConstants.NOT_FOUND));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING + "/1")
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(UserConstants.NOT_FOUND, result.getResponse().getContentAsString());
    ;
  }

  /**
   * * getbyId olmayan kullanıcı çağrımı.
   *
   * @throws Exception Hata döndürür.
   */
  @Test
  public void getUserByIdUnknown() throws Exception {
    Mockito.when(userService.getById(1)).thenThrow(new UserException(HttpStatus.BAD_REQUEST.value(), UserConstants.NOT_FOUND));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING + "/1")
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(400, result.getResponse().getStatus());
  }

  /**
   * Başarılı kayıt testi.
   */
  @Test
  public void createUserTest() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(UserController.USERS_MAPPING)
        .content(asJsonString(mockUserList.get(0)))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals("1", result.getResponse().getContentAsString());
    ;
  }

  /**
   * Başarısız kayıt testi.
   */
  @Test
  public void createUserFailTest() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(UserController.USERS_MAPPING)
        .content(asJsonString(mockUserList.get(1)))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(400, result.getResponse().getStatus());
  }

  /**
   * Business hata donüş kayıt testi.
   */
  @Test
  public void createUserFailConflictTest() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(UserController.USERS_MAPPING)
        .content(asJsonString(mockUserList.get(2)))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(409, result.getResponse().getStatus());
  }

  /**
   * Business hata donüş kayıt testi.
   */
  @Test
  public void createUserFailRequiredTest() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(UserController.USERS_MAPPING)
        .content(asJsonString(mockUserList.get(2)))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(409, result.getResponse().getStatus());
  }

  /**
   * Kullanıcı bilgilerinin aktifleştirme testi.
   */
  @Test
  public void activeUserdTest() throws Exception {
    Mockito.when(userService.activatedUser(1)).thenReturn(1);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put(UserController.USERS_MAPPING + "/1/activate")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * Kullanıcı bilgilerinin pasifleştirme testi.
   */
  @Test
  public void inActiveUserdTest() throws Exception {
    Mockito.when(userService.inActivatedUser(1)).thenReturn(1);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put(UserController.USERS_MAPPING + "/1/pacify")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * UserDto üretilmesi için yazılmıştır.
   */
  public UserDto getUserDto(Integer id, String userName, String name, String surName,
                            String phone, String mobilePhone, String email, boolean active) {
    UserDto userDto = new UserDto();
    userDto.setId(id);
    userDto.setSurName(surName);
    userDto.setUserName(userName);
    userDto.setName(name);
    userDto.setPhone(phone);
    userDto.setMobilePhone(mobilePhone);
    userDto.setEmail(email);
    userDto.setActive(active);
    return userDto;
  }

  /**
   * Objeyi json'a çevirir.
   */
  public static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
