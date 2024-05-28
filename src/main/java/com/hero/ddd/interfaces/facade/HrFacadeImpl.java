package com.hero.ddd.interfaces.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hero.ddd.domain.hrmanage.OnboardingService;
import com.hero.ddd.domain.hrmanage.context.OnboardDTO;
import com.hero.ddd.infrastructure.utils.JavaBean;
import com.hero.ddd.interfaces.web.request.OnboardRequest;

@Service
public class HrFacadeImpl implements HrFacade {

  @Autowired
  private OnboardingService onboardingService;

  @Override
  public void onboard(OnboardRequest request) {
    this.onboardingService.onboard(JavaBean.copyFrom(request, OnboardDTO::new));
  }

}
