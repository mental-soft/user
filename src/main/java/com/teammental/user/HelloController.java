package com.teammental.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hcguler on 7/8/2017.
 */
@Controller
public class HelloController {

  /**
   * index.xhtml sayfasını dönen metoddur.
   * @return sayfa adı geri döndürür
   */
  @RequestMapping({"/","/index"})
  public String getIndex() {
    return "index";
  }

}

