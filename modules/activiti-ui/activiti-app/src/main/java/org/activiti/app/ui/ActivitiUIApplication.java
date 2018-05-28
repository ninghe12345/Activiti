package org.activiti.app.ui;

import org.activiti.app.conf.ApplicationConfiguration;
import org.activiti.app.servlet.ApiDispatcherServletConfiguration;
import org.activiti.app.servlet.AppDispatcherServletConfiguration;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * @author jimmy
 **/
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
})
@Import({ApplicationConfiguration.class})
@EnableTransactionManagement
public class ActivitiUIApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ActivitiUIApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ActivitiUIApplication.class);
    }



    @Bean
    public ServletRegistrationBean apiDispatcher() {
        DispatcherServlet api = new DispatcherServlet();
        api.setContextClass(AnnotationConfigWebApplicationContext.class);
        api.setContextConfigLocation(ApiDispatcherServletConfiguration.class.getName());
        ServletRegistrationBean apiDispatcher = new ServletRegistrationBean(api);
        apiDispatcher.addUrlMappings("/api/*");
        apiDispatcher.setLoadOnStartup(1);
        apiDispatcher.setAsyncSupported(true);
        apiDispatcher.setName("api");
        return apiDispatcher;
    }

    @Bean
    public ServletRegistrationBean appDispatcher() {
        DispatcherServlet app = new DispatcherServlet();
        app.setContextClass(AnnotationConfigWebApplicationContext.class);
        app.setContextConfigLocation(AppDispatcherServletConfiguration.class.getName());

        ServletRegistrationBean appDispatcher = new ServletRegistrationBean(app);
        appDispatcher.addUrlMappings("/app/*");
        appDispatcher.setLoadOnStartup(1);
        appDispatcher.setAsyncSupported(true);
        appDispatcher.setName("app");
        return appDispatcher;
    }
    @Bean
    public FilterRegistrationBean openEntityManagerInViewFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new OpenEntityManagerInViewFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("openEntityManagerInViewFilter");
        filterRegistrationBean.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC));
        filterRegistrationBean.setOrder(-200);
        return filterRegistrationBean;
    }


}
