package com.personal.springframework.repository;

import com.personal.springframework.model.Agency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:UserMapper
 * @author: anshaojun
 * @time: 2021-05-18 16:42
 */
@Mapper
public interface AgencyMapper extends BaseMapper<Agency>{
    List<Agency> loadAgencyTree();

    int deleteByParent(@Param("parentId") String parentId);

    List<Agency> getAgenciesByParent(@Param("agencyId") String agencyId);
}
