package com.teammental.user.config;

import com.teammental.meconfig.handler.rest.EntityDeleteExceptionRestHandler;
import com.teammental.meconfig.handler.rest.EntityInsertExceptionRestHandler;
import com.teammental.meconfig.handler.rest.EntityNotFoundExceptionRestHandler;
import com.teammental.meconfig.handler.rest.EntityUpdateExceptionRestHandler;
import com.teammental.meconfig.handler.rest.InvalidDataAccessApiUsageExceptionRestHandler;
import com.teammental.meconfig.handler.rest.ValidationErrorRestHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by hcguler on 11/19/2017.
 */
@TestConfiguration
@Import({EntityNotFoundExceptionRestHandler.class,
    EntityInsertExceptionRestHandler.class,
    ValidationErrorRestHandler.class,
    EntityUpdateExceptionRestHandler.class,
    EntityDeleteExceptionRestHandler.class,
    InvalidDataAccessApiUsageExceptionRestHandler.class})
public class TestControllerConfig {

}
