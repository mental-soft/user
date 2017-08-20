package com.teammental.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
/**
 * Created by hcguler on 7/8/2017.
 */

/**
 * Test classı.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
  @Autowired
  private MockMvc mvc;

  /**
   * test metodudur.
   * @throws Exception
   */
  @Test
  public void getIndex() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/")
        .accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk());
  }
}
