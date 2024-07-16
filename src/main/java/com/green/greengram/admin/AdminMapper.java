package com.green.greengram.admin;

import com.green.greengram.admin.model.GetAdminProviderRes;
import com.green.greengram.admin.model.GetAdminReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<GetAdminProviderRes> getAdminProvider(GetAdminReq req) ;
}
