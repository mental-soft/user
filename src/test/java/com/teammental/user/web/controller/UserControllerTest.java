package com.teammental.user.web.controller;

/**
 * Created by hcguler on 8/5/2017.
 */

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teammental.meconfig.exception.entity.EntityNotFoundException;
import com.teammental.user.config.TestControllerConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Test classı.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
@Import(TestControllerConfig.class)
public class UserControllerTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTest.class);
  List<UserDto> mockUserList;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UserService userService;

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
    Mockito.when(userService.insert(any(UserDto.class)))
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
    Mockito.when(userService.findAll()).thenReturn(mockUserList);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserConstants.BASE_URL)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * Mocklanan değerler için dönüş alınan testtir.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test
  public void getAll_shouldReturn404_whenNotFoundAnyTitle() throws Exception {
    when(userService.findAll())
        .thenThrow(new EntityNotFoundException());

    mockMvc.perform(get(UserConstants.BASE_URL))
        .andExpect(status().isNotFound());

    verify(userService, times(1)).findAll();
    verifyNoMoreInteractions(userService);
  }

  /**
   * Service katmanından exception yollandığında request test ediliyor.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test(expected = Exception.class)
  public void getAllUsersException() throws Exception {
    Mockito.when(userService.findAll()).thenThrow(new Exception("Hata"));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserConstants.BASE_URL)
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
  }

  /**
   * findById unit test.
   *
   * @throws Exception Hata dönüşüdür.
   */
  @Test
  public void getUserById() throws Exception {
    Mockito.when(userService.findById(1)).thenReturn(mockUserList.get(0));
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(UserConstants.BASE_URL + "/1")
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    LOGGER.info(result.getResponse().getContentAsString());
    assertEquals(200, result.getResponse().getStatus());
    ;
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
}
