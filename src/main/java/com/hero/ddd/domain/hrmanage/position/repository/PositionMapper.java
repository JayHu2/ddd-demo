package com.hero.ddd.domain.hrmanage.position.repository;

import org.apache.ibatis.annotations.Mapper;

import com.hero.ddd.domain.hrmanage.position.data.PositionDO;

@Mapper
interface PositionMapper {

  public PositionDO findPositionByCode(String positionCode);
  
}
