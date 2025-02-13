package vn.project.group_lottery.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.project.group_lottery.dto.UserDTOSession;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.repository.UserRepository;
import vn.project.group_lottery.service.SessionService;

@Component
public class SessionUpdateInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final SessionService sessionService;

    public SessionUpdateInterceptor(UserRepository userRepository, SessionService sessionService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            User user = this.userRepository.findUserByUsername(username);
            if (user == null) {
                return true;
            }

            UserDTOSession userSession = (UserDTOSession) session.getAttribute("userDTOSession");

            if (userSession == null ||
                    userSession.getBalance() != user.getWallet().getBalance() ||
                    !userSession.getUsername().equals(user.getUsername()) ||
                    !userSession.getAvatar().equals(user.getAvatar())) {

                sessionService.setSessionUser(session, user);
            }
        }

        return true;
    }
}
