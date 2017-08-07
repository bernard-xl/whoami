package xl.application.hr.whoami.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SimpleLoginSuccessHandler loginSuccessHandler;

    @Autowired
    private SimpleLoginFailureHandler loginFailureHandler;

    @Autowired
    private SimpleLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/manage/**").authenticated()
            .anyRequest().permitAll()
        .and()
            .formLogin()
            .loginPage("/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
        .and()
            .logout()
            .logoutUrl("/auth/logout")
            .logoutSuccessHandler(logoutSuccessHandler)
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        .and()
            .csrf()
            .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("ADMIN", "ACTUATOR");
    }
}
