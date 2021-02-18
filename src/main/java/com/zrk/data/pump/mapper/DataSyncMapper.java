package com.zrk.data.pump.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zrk.data.pump.pojo.DataSync;
import com.zrk.data.pump.pojo.DataSyncExample;

public interface DataSyncMapper {
    long countByExample(DataSyncExample example);

    int deleteByExample(DataSyncExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataSync record);

    int insertSelective(DataSync record);

    List<DataSync> selectByExample(DataSyncExample example);

    DataSync selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataSync record, @Param("example") DataSyncExample example);

    int updateByExample(@Param("record") DataSync record, @Param("example") DataSyncExample example);

    int updateByPrimaryKeySelective(DataSync record);

    int updateByPrimaryKey(DataSync record);
}