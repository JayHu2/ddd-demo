package com.hero.ddd.interfaces.facade;

import com.hero.ddd.interfaces.web.request.OnboardRequest;

public interface HrFacade {

  // 入职服务
  void onboard(OnboardRequest request);
}
