//package com.spc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/customers/**").authenticated()  // Secure customer endpoints
//                .anyRequest().permitAll()
//                .and()
//                .httpBasic()  // Use HTTP Basic authentication
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.setStatus(401);  // 401 Unauthorized
//                    response.setHeader("WWW-Authenticate", "Basic realm=\"Access to the customer resource\"");
//                    response.getWriter().write("{\"error\": \"Authentication required\", \"message\": \"You must authenticate to access this resource.\"}");
//                });
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
