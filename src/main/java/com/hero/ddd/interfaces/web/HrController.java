package com.hero.ddd.interfaces.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hero.ddd.interfaces.facade.HrFacade;
import com.hero.ddd.interfaces.web.request.AddBankCardRequest;
import com.hero.ddd.interfaces.web.request.OnboardRequest;

@Controller
public class HrController {

  // 在多种业务心态下，可以用策略模式遴选出符合期望的 HrFacade 实现
  @Autowired
  private HrFacade hrFacade;

  @PostMapping("/employee/onboard")
  protected void onboard(@RequestBody OnboardRequest request, @RequestHeader(name = "X-Logined-UserId") String loginedUserId) {
    // 对登录用户进行认证&鉴权
    // 入职服务调用
    this.hrFacade.onboard(request);
  }

  @PostMapping("/employee/{employeeCode}/addBankCard")
  protected void addBankCard(
      @PathVariable(name = "employeeCode") String employeeCode,
      @RequestBody AddBankCardRequest request, @RequestHeader(name = "X-Logined-UserId") String loginedUserId) {
    // 对登录用户进行认证&鉴权
    // 添加银行卡服务调用
    this.hrFacade.addBankCard(employeeCode, request);
  }
}
