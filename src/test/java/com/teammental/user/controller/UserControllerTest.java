package com.teammental.user.controller;

/**
 * Created by hcguler on 8/5/2017.
 */

import static org.junit.Assert.assertEquals;

import com.teammental.user.dto.UserDto;
import com.teammental.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
  public void init() {
    mockUserList = new ArrayList<>();
    UserDto e = new UserDto();
    e.setId(1);
    e.setMobilePhone("1");
    e.setActive(true);
    e.setEmail("asda");
    e.setUserName("asda");
    e.setName("asda");
    e.setSurName("asda");
    e.setPhone("asda");
    mockUserList.add(e);
  }

  /**
   * Mocklanan değerler için dönüş alınan testtir.
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
   * @throws Exception Hata dönüşüdür.
   */
  @Test
  public void getAllUsersFail() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals("Herhangi bir kullanıcı bulunamadı.", result.getResponse().getContentAsString());
  }

  /**
   * Service katmanından exception yollandığında request test ediliyor.
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
    assertEquals(200, result.getResponse().getStatus());;
  }

  /**
   * * getbyId olmayan kullanıcı çağrımı.
   * @throws Exception Hata döndürür.
   */
  @Test
  public void getUserByIdUnknown() throws Exception {
    Mockito.when(userService.getById(1)).thenReturn(mockUserList.get(0));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserController.USERS_MAPPING + "/2")
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(400, result.getResponse().getStatus());;
  }
}
