package com.hero.ddd.interfaces.facade;

import com.hero.ddd.interfaces.web.request.AddBankCardRequest;
import com.hero.ddd.interfaces.web.request.OnboardRequest;

public interface HrFacade {

  // 入职服务
  void onboard(OnboardRequest request);

  // 添加银行卡
  void addBankCard(String employeeCode, AddBankCardRequest request);
}
