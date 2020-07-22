package ru.tishtech.developerhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocDonateController {

  @GetMapping("/doc")
  public String docPage() {
    return "doc";
  }

  @GetMapping("/donate")
  public String donatePage() {
    return "donate";
  }
}
