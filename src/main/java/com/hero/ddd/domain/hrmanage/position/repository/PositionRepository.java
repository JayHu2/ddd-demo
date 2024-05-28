package com.hero.ddd.domain.hrmanage.position.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hero.ddd.domain.hrmanage.position.Position;
import com.hero.ddd.domain.hrmanage.position.data.PositionDO;

@Repository
public class PositionRepository {

  @Autowired
  private PositionMapper positionMapper;

  private Optional<PositionDO> findPositionByCode(String positionCode) {
    return Optional.ofNullable(positionMapper.findPositionByCode(positionCode));
  }

  public Optional<Position> findPosition(String positionCode) {
    return this.findPositionByCode(positionCode).map(orgDO -> {
      return new Position();
    });
  }
}
