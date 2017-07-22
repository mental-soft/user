package com.teammental.user.jpa;

import com.teammental.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User jpa repository.
 * Created by hcguler on 7/22/2017.
 */
public interface UserRepostory extends JpaRepository<User, Integer> {

}
