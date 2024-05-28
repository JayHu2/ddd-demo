package com.hero.ddd.domain.hrmanage.serve.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hero.ddd.domain.hrmanage.serve.data.ServeRecordDO;

@Mapper
interface ServeRecordMapper {

  public List<ServeRecordDO> findServeRecordsByEmployeeCode(String employeeCode);
  
}
