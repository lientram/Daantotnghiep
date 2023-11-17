package com.ps20652.DATN.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserDetailsService userDetailsService;
        
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        
        @Bean
        public SpringSecurityDialect springSecurityDialect(){
            return new SpringSecurityDialect();
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
//                    .antMatchers("/", "/feedback/**", "/product/**", "/security/**","/ProductDetails/**").permitAll()
                    .antMatchers("/cart","/pay", "/admin/**").authenticated()
                    .antMatchers("/admin/**").hasAnyRole("ADMIN", "STAFF")
                    .anyRequest().permitAll()
                .and()
                .formLogin()
                	.loginPage("/login")
                	.defaultSuccessUrl("/")
                	.permitAll()
                .and()
               	.logout()
                	.logoutUrl("/logout")
                	.logoutSuccessUrl("/")
               	.and()
                .exceptionHandling()
                	.accessDeniedPage("/access-denied") // Chuyển hướng đến trang access-denied khi bị cấm truy cập (403)
                .and()
                .csrf().disable();
    }
    }


