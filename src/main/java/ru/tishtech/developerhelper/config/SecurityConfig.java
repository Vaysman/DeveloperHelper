package ru.tishtech.developerhelper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tishtech.developerhelper.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private UserService userService;

  @Lazy @Autowired private PasswordEncoder passwordEncoder;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(8);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(
            "/",
            "/registration",
            "/password/**",
            "/doc",
            "/donate",
            "/contact",
            "/activate/*",
            "/img/**",
            "/css/**",
            "/webjars/**",
            "/favicon.ico",
            "/actuator/**",
            "/public/**",
            "/h2-console/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .csrf()
        .ignoringAntMatchers("/h2-console/**")
        .and()
        .headers()
        .frameOptions()
        .sameOrigin()
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/hello")
        .permitAll()
        .and()
        .logout()
        .permitAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
  }
}
