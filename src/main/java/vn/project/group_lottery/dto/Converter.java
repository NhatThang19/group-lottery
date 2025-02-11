package vn.project.group_lottery.dto;

import vn.project.group_lottery.dto.Request.RegisterInfoReq;
import vn.project.group_lottery.dto.Request.RegisterReq;
import vn.project.group_lottery.dto.Response.UserRes;
import vn.project.group_lottery.dto.Response.UserSessionRes;
import vn.project.group_lottery.enums.Gender;
import vn.project.group_lottery.model.User;

public class Converter {
    public static User registerReqConvertToUser(User user, RegisterReq registerReq, RegisterInfoReq registerInfoReq) {
        user.setUsername(registerReq.getUsername());
        user.setPassword(registerReq.getPassword());

        user.setAddress(registerInfoReq.getAddress());
        user.setDateOfBirth(registerInfoReq.getDateOfBirth());
        user.setEmail(registerInfoReq.getEmail());
        user.setGender(Gender.valueOf(registerInfoReq.getGender()));
        user.setPhone(registerInfoReq.getPhone());

        return user;
    }

    public static UserSessionRes userConvertTSessionRes(User user, UserSessionRes userSessionRes) {
        userSessionRes.setId(user.getId());
        userSessionRes.setUsername(user.getUsername());
        userSessionRes.setAvatar(user.getAvatar());
        userSessionRes.setBalance(user.getWallet().getBalance());

        return userSessionRes;
    }

    public static UserRes userConvertToUserDetailRes(User user, UserRes userRes) {
        return userRes;
    }

    public static UserRes userConvertToUserRes(User user, UserRes userRes) {
        userRes.setId(user.getId());
        userRes.setUsername(user.getUsername());
        userRes.setEmail(user.getEmail());
        userRes.setPhone(user.getPhone());
        userRes.setDateOfBirth(user.getDateOfBirth());
        userRes.setAddress(user.getAddress());
        userRes.setGender(user.getGender().toString());
        userRes.setAvatar(user.getAvatar());
        userRes.setStatus(user.getStatus().toString());
        userRes.setLastLogin(user.getLastLogin());
        userRes.setCreatedBy(user.getCreatedBy());
        userRes.setCreatedDate(user.getCreatedDate());
        userRes.setLastModifiedBy(user.getLastModifiedBy());
        userRes.setLastModifiedDate(user.getLastModifiedDate());
        userRes.setRoleId(user.getRole().getId());
        userRes.setBalance(user.getWallet().getBalance());

        return userRes;
    }
}
