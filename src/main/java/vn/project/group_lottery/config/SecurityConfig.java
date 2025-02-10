package vn.project.group_lottery.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import vn.project.group_lottery.service.CustomUserDetailsService;
import vn.project.group_lottery.service.UserService;

@Configuration
public class SecurityConfig {
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(UserService userService) {
                return new CustomUserDetailsService(userService);
        }

        @Bean
        public AuthenticationSuccessHandler customSuccessHandler() {
                return new CustomSuccessHandler();
        }

        @Bean
        public DaoAuthenticationProvider authProvider(
                        PasswordEncoder passwordEncoder,
                        UserDetailsService userDetailsService) {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder);
                authProvider.setHideUserNotFoundExceptions(false);
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                return new ProviderManager(
                                Collections.singletonList(authProvider(passwordEncoder, userDetailsService)));
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService)
                        throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/client/**",
                                                                "/common/**",
                                                                "/auth/**",
                                                                "/")
                                                .permitAll()
                                                .requestMatchers("/admin/**", "/admin/assets/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/auth/login")
                                                .successHandler(customSuccessHandler())
                                                .failureHandler((request, response, exception) -> {
                                                        request.getSession().setAttribute("loginErrorMessage",
                                                                        "Tên đăng nhập hoặc mật khẩu không đúng!");
                                                        response.sendRedirect("/auth/login");
                                                })
                                                .permitAll())
                                .rememberMe(remember -> remember
                                                .key("uniqueAndSecret")
                                                .tokenValiditySeconds(30 * 24 * 60 * 60)
                                                .rememberMeParameter("remember-me")
                                                .userDetailsService(userDetailsService))
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        request.getSession().setAttribute("logoutSuccessMessage",
                                                                        "Đăng xuất thành công!");
                                                        response.sendRedirect("/auth/login");
                                                })
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .permitAll())
                                .sessionManagement((sessionManagement) -> sessionManagement
                                                .invalidSessionUrl("/logout?expired")
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(1).expiredUrl("/auth/login?expired=true")
                                                .maxSessionsPreventsLogin(false));
                return http.build();
        }
}
