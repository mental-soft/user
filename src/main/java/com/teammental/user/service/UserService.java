package com.teammental.user.service;

import com.teammental.user.dto.UserDto;
import java.util.List;

/**
 * User bilgilerine ait işlemlerin rest olarak dış sistemlere sağlandığı service metodlarını içerir.
 * Created by hcguler on 8/5/2017.
 */
public interface UserService {

  List<UserDto> getAll();

  UserDto getById(int userId) throws Exception;

  void deleteById(int userId);

  int saveOrUpdate(UserDto userDto) throws Exception;

  int activatedUser(int userId) throws Exception;

  int inActivatedUser(int userId) throws Exception;

  int isExistUser(UserDto dto) throws Exception;

}
