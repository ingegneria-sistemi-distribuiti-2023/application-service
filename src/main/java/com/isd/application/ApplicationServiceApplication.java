package com.isd.application;

import com.isd.application.auth.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class ApplicationServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationServiceApplication.class, args);
    }

    /**
     * Register custom filter on SessionService
     * */
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterFilterRegistrationBean(){
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        // TODO:
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

}
