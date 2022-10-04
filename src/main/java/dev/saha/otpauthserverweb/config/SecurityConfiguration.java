package dev.saha.otpauthserverweb.config;


import dev.saha.otpauthserverweb.security.AuthEntryPointJwt;
import dev.saha.otpauthserverweb.security.AuthTokenFilter;
import dev.saha.otpauthserverweb.security.service.UserDetailsServiceImpl;
import dev.saha.otpauthserverweb.util.SecurityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;

    private AuthEntryPointJwt authEntryPointJwt;

    private AuthTokenFilter authenticationJwtTokenFilter;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt authEntryPointJwt, AuthTokenFilter authenticationJwtTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.authEntryPointJwt = authEntryPointJwt;
        this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()  throws Exception{
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(SecurityConstant.PUBLIC_URI).permitAll()
                .antMatchers("/auth-dependent-routes").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable();

        http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/auth","/auth/**","/api/auth/**","/webjars/**","/swagger-ui/")
                .and().ignoring().antMatchers("/auth","/auth/**");
    }

}