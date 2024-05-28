package com.hero.ddd.domain.hrmanage.organization.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hero.ddd.domain.hrmanage.organization.Organization;
import com.hero.ddd.domain.hrmanage.organization.data.OrganizationDO;

@Repository
public class OrganizationRepository {

  @Autowired
  private OrganizationMapper organizationMapper;

  private Optional<OrganizationDO> findOrganizationByCode(String orgCode) {
    return Optional.ofNullable(organizationMapper.findOrganizationByCode(orgCode));
  }

  private List<OrganizationDO> findChildDOsByOrgCode(String orgCode) {
    return organizationMapper.findChildsByOrgCode(orgCode);
  }

  public Optional<Organization> findOrganization(String orgCode) {
    return this.findOrganizationByCode(orgCode).map(orgDO -> {
      return new Organization(orgDO.getCode(), orgDO.getName(), orgDO.getParentCode(),
          () -> this.findChildsByOrgCode(orgDO.getCode()));
    });
  }

  private List<Organization> findChildsByOrgCode(String orgCode) {
    return this.findChildDOsByOrgCode(orgCode).stream().map(orgDO -> {
      return new Organization(orgDO.getCode(), orgDO.getName(), orgDO.getParentCode(),
          () -> this.findChildsByOrgCode(orgDO.getCode()));
    }).collect(Collectors.toList());
  }
}
