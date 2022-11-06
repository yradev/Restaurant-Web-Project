package source.restaurant_web_project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import source.restaurant_web_project.service.impl.AuthServiceIMPL;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class AuthConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthServiceIMPL myUserDetailsService;

    @Autowired
    public AuthConfiguration(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder, AuthServiceIMPL myUserDetailsService) {
        this.dataSource = dataSource;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/auth/logout").authenticated()
                .antMatchers("/delivery/**").authenticated()
                .antMatchers("/reservation/**").authenticated()
                .antMatchers("/owner/**").hasRole("OWNER")
                .antMatchers("/staff/**").hasRole("STAFF")
                .and()
                .anonymous()
                .principal("Guest")
                .and()
                  .formLogin()
                .successForwardUrl("/home")
                 .loginPage("/auth/login")
                 .failureForwardUrl("/auth/login/error")
                    .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .key("remember Me Encryption Key")
                .rememberMeCookieName("rememberMe")
                .tokenValiditySeconds(10000)
                .and()
                    .logout()
                    .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
                .invalidateHttpSession(true)

                .deleteCookies("JSESSIONID")

                .and().csrf().disable();
    }
}
