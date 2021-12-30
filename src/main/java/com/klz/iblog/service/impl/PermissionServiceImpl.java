package com.klz.iblog.service.impl;

//import com.klz.iblog.mapper.PermissionMapper;
//import com.klz.iblog.service.PermissionMapperService;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Service
//public class PermissionServiceImpl implements PermissionMapperService {
//
//    @Resource
//    PermissionMapper permissionMapper;
//
//    @Override
//    public List<PermissionDto> selectPermissionByRoleId(Integer roleId) {
//        if(roleId == null){
//            throw new CustomException("selectPermissionByRoleId的参数roleId为null");
//        }
//        try{
//            return permissionMapper.selectPermissionByRoleId(roleId);
//        }catch (Exception e){
//            throw new CustomException("selectPermissionByRoleId查询角色权限异常:"+e.getMessage());
//        }
//    }
//}
