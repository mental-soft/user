package com.teammental.user.service;

import com.teammental.user.dto.UserDto;
import java.util.List;

/**
 * User bilgilerine ait işlemlerin rest olarak dış sistemlere sağlandığı service metodlarını içerir.
 * Created by hcguler on 8/5/2017.
 */
public interface UserService {

  List<UserDto> getAll();

  UserDto getById(int userId);

  int saveOrUpdate(UserDto userDto);

  int activatedUser(int userId);

  int inActivatedUser(int userId);
}
