package com.hero.ddd.interfaces.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hero.ddd.domain.hrmanage.AddBankCardService;
import com.hero.ddd.domain.hrmanage.OnboardingService;
import com.hero.ddd.domain.hrmanage.context.BankCardDTO;
import com.hero.ddd.domain.hrmanage.context.OnboardDTO;
import com.hero.ddd.infrastructure.utils.JavaBean;
import com.hero.ddd.interfaces.web.request.AddBankCardRequest;
import com.hero.ddd.interfaces.web.request.OnboardRequest;

@Service
public class HrFacadeImpl implements HrFacade {

  @Autowired
  private OnboardingService onboardingService;

  @Autowired
  private AddBankCardService addBankCardService;

  @Override
  public void onboard(OnboardRequest request) {
    // 上下文映射
    OnboardDTO onboardContextMapping = JavaBean.copyFrom(request, OnboardDTO::new);
    this.onboardingService.onboard(onboardContextMapping);
  }

  @Override
  public void addBankCard(String employeeCode, AddBankCardRequest request) {
    // 上下文映射
    BankCardDTO bankCardContextMapping = JavaBean.copyFrom(request, BankCardDTO::new);
    this.addBankCardService.addBankCard(employeeCode, bankCardContextMapping);
  }

}
