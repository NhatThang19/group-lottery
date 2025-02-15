package vn.project.group_lottery.dto;

import java.util.List;

import vn.project.group_lottery.dto.Request.RegisterInfoReq;
import vn.project.group_lottery.dto.Request.RegisterReq;
import vn.project.group_lottery.enums.Gender;
import vn.project.group_lottery.model.Ticket;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.repository.TicketRepository;

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

    public static UserDTOSession userConvertToUserSession(User user, UserDTOSession userDTOSession) {
        userDTOSession.setId(user.getId());
        userDTOSession.setUsername(user.getUsername());
        userDTOSession.setAvatar(user.getAvatar());
        userDTOSession.setBalance(user.getWallet().getBalance());

        return userDTOSession;
    }

    public static UserDTO userConvertToUserDTODetail(User user, UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setGender(user.getGender().toString());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setStatus(user.getStatus().toString());
        userDTO.setLastLogin(user.getLastLogin());
        userDTO.setRole(user.getRole().getName());
        userDTO.setCreatedDate(user.getCreatedDate());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        userDTO.setLastModifiedBy(user.getLastModifiedBy());
        userDTO.setAddress(user.getAddress());
        userDTO.setBalance(user.getWallet().getBalance());

        return userDTO;
    }

    public static UserDTO userConvertToUserDTO(User user, UserDTO userRes) {
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
        userRes.setRole(user.getRole().getName());
        userRes.setBalance(user.getWallet().getBalance());

        return userRes;
    }
}
