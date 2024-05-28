package com.hero.ddd.domain.hrmanage.organization.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hero.ddd.domain.hrmanage.organization.data.OrganizationDO;

@Mapper
interface OrganizationMapper {

  public OrganizationDO findOrganizationByCode(String orgCode);
  
  public List<OrganizationDO> findChildsByOrgCode(String orgCode);
}
