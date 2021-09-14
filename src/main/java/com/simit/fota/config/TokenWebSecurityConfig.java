package com.simit.fota.config;

import com.simit.fota.filter.TokenAuthFilter;
import com.simit.fota.filter.TokenLoginFilter;
import com.simit.fota.filter.TokenLogoutHandler;
import com.simit.fota.redis.RedisService;
import com.simit.fota.security.DefaultPasswordEncoder;
import com.simit.fota.security.UnauthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthEntryPoint())//没有权限访问
                .and().csrf().disable()
                .cors().and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/fota/api/user/logout")//退出路径
                .addLogoutHandler(new TokenLogoutHandler(redisService)).and()
                .addFilter(new TokenLoginFilter(authenticationManager(),redisService))
                .addFilter(new TokenAuthFilter(authenticationManager(),redisService)).httpBasic();


//        http.formLogin() //自定义自己编写的登陆页面
//                .loginProcessingUrl("/fota/api/user/login")//登陆访问路径
////                .defaultSuccessUrl("/test/hello").permitAll()//登陆成功跳转
//                .and().authorizeRequests()//那些需要认证
//                .antMatchers("/test/index").hasRole("sale")
//                .anyRequest().authenticated()//所有请求都可以访问
//                .and()
//                .userDetailsService(userDetailsService)
//                .csrf().disable();

//        http.formLogin() //自定义自己编写的登陆页面
//                .loginProcessingUrl("/api/user/login")//登陆访问路径
//                .defaultSuccessUrl("/test/hello").permitAll();//登陆成功跳转

//        http.exceptionHandling()
//                .and().csrf().disable()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and().formLogin().loginProcessingUrl("/fota/api/user/login").defaultSuccessUrl("/test/hello")
//                .and().authorizeRequests().antMatchers("/test/hello").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    //不进行认证的路径，可以直接访问
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**");
        web.ignoring().antMatchers("/fota/api/user/signup");
        web.ignoring().antMatchers("/swagger*/**");
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/webjars/**");
        web.ignoring().antMatchers("/webjars/**");
        web.ignoring().antMatchers("/v2/**");
        web.ignoring().antMatchers("/api/**");

    }
}
