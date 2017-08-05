package com.teammental.user.jpa;

import com.teammental.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * User jpa repository.
 * Created by hcguler on 7/22/2017.
 */
@Repository
public interface UserRepostory extends JpaRepository<User, Integer> {
  List<User> findByEmail(String email);
  List<User> findByUserName(String userName);
  List<User> findByMobilePhone(String mobilePhone);
}
