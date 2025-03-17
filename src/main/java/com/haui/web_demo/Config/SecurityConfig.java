
package com.haui.web_demo.Config;
import com.haui.web_demo.Service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public SecurityConfig(CustomerUserDetailsService customerUserDetailsService) {
        this.customerUserDetailsService = customerUserDetailsService;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Các URL cho phép truy cập mà không cần đăng nhập
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/img/**", "/font/**", "font/js/**", "/images/**", "/search", "/error", "/favicon.ico", "/authorroom", "/receive_rooms", "/ownroom", "/about", "/find_rooms",
                                "/indexuser", "/menu", "/blog", "/detailroom", "/review", "/partner", "/audio", "/login", "/register", "/loginuser", "/admin/forgot_password")
                        .permitAll()
                        .requestMatchers("/addblog", "/table_review", "/addpartner", "/addreview", "/detail_blog", "/filter_price", "/filter_acreage", "/filter_type", "/filter_add", "/editblog").permitAll()
                        // Các URL chỉ cho phép admin truy cập
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/edituser/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/edituser").hasAnyAuthority("ADMIN")
                        // Các URL yêu cầu phải đăng nhập
                        .anyRequest().authenticated()
                )
//                .formLogin(formLogin -> formLogin
//                        // Đăng nhập cho admin
//                        .loginPage("/login")
//                        .loginProcessingUrl("/login")
//                        .usernameParameter("phone")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/admin", true)
//                        // Đăng nhập cho user
//                        .loginPage("/loginuser")
//                        .loginProcessingUrl("/loginuser")
//                        .usernameParameter("phone")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/indexuser", true)
//                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginuser") // Dùng chung 1 trang đăng nhập
                        .loginProcessingUrl("/loginuser")
                        .usernameParameter("phone")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
                                response.sendRedirect("/admin");
                            } else {
                                response.sendRedirect("/indexuser");
                            }
                        })
                )

//                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/loginuser"));
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null && authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                                response.sendRedirect("/loginuser"); // Admin logout -> về loginuser
                            } else {
                                response.sendRedirect("/indexuser"); // User logout -> về index
                            }
                        })
                );
        return http.build();
    }


    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**", "/assets/**", "/css/**", "/js/**", "/img/**", "/images/**", "/font/**");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


}
