package vn.project.group_lottery.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.project.group_lottery.dto.Converter;
import vn.project.group_lottery.dto.UserDTOSession;
import vn.project.group_lottery.model.User;

@Service
public class SessionService {

    public void setSessionUser(HttpSession session, User user) {
        UserDTOSession userDTOSession = new UserDTOSession();
        Converter.userConvertToUserSession(user, userDTOSession);
        session.setAttribute("userDTOSession", userDTOSession);
    }
}
