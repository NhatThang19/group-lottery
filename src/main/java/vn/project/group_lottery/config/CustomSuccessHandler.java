package vn.project.group_lottery.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.service.SessionService;
import vn.project.group_lottery.service.UserService;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        User user = this.userService.getUserByUsername(username);

        user.setLastLogin(LocalDateTime.now());
        this.userService.updateUser(user);

        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 24 * 60 * 60);

        sessionService.setSessionUser(session, user);

        String targetUrl = determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_USER"))
                .findFirst()
                .map(role -> role.equals("ROLE_ADMIN") ? "/admin" : "/")
                .orElse("/login?error=true");
    }

}
