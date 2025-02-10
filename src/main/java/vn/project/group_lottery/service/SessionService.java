package vn.project.group_lottery.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.project.group_lottery.dto.Converter;
import vn.project.group_lottery.dto.Response.UserSessionRes;
import vn.project.group_lottery.model.User;

@Service
public class SessionService {
    public void setSessionUser(HttpSession session, User user) {
        UserSessionRes userSessionRes = new UserSessionRes();
        Converter.userConvertTSessionRes(user, userSessionRes);
        session.setAttribute("userSessionRes", userSessionRes);
    }
}
