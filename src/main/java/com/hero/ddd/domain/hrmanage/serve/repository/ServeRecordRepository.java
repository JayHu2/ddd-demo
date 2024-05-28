package com.hero.ddd.domain.hrmanage.serve.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hero.ddd.domain.hrmanage.serve.ServeRecord;

@Repository
public class ServeRecordRepository {

  @Autowired
  private ServeRecordMapper recordMapper;

  public List<ServeRecord> findServeRecordsByEmployeeCode(String employeeCode) {
    return recordMapper.findServeRecordsByEmployeeCode(employeeCode).stream().map(record -> {
      return new ServeRecord();
    }).collect(Collectors.toList());
  }

  public void save(ServeRecord record) {
    // 如果是数据库持久化，建议通过预测数据库影响行数来判断持久层结果是否符合业务预期
  }

}
