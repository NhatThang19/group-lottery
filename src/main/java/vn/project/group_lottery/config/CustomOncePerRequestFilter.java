package vn.project.group_lottery.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.project.group_lottery.enums.UserStatus;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.service.UserService;

public class CustomOncePerRequestFilter extends OncePerRequestFilter {
    private final UserService userService;

    public CustomOncePerRequestFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);

            // Nếu user null -> xóa context và redirect về trang login
            if (user == null) {
                SecurityContextHolder.clearContext();
                response.sendRedirect("/auth/login?error=user_not_found");
                return;
            }

            // Nếu user bị ban -> xóa session, xóa context và redirect về trang login
            if (user.getStatus() == UserStatus.banned) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                SecurityContextHolder.clearContext();
                response.sendRedirect("/auth/login?banned=true");
                return;
            }
        }

        // Tiếp tục request nếu user hợp lệ
        filterChain.doFilter(request, response);
    }
}
