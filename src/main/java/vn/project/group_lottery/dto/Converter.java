package vn.project.group_lottery.dto;

import vn.project.group_lottery.dto.Request.RegisterInfoReq;
import vn.project.group_lottery.dto.Request.RegisterReq;
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
}
