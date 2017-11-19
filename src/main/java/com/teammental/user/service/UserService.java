package com.teammental.user.service;

import com.teammental.meconfig.bll.service.BaseCrudService;
import com.teammental.user.dto.UserDto;

/**
 * User bilgilerine ait işlemlerin rest olarak dış sistemlere sağlandığı service metodlarını içerir.
 * Created by hcguler on 8/5/2017.
 */
public interface UserService extends BaseCrudService<UserDto, Integer> {

  int activatedUser(Integer userId);

  int inActivatedUser(Integer userId);
}
