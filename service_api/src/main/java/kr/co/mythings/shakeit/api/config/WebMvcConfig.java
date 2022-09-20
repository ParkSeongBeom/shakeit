package kr.co.mythings.shakeit.api.config;

import kr.co.mythings.shakeit.api.config.interceptor.AuthenticatedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.Objects;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticatedInterceptor authenticatedInterceptor;

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setDetectAllHandlerAdapters(true);
        dispatcherServlet.setDetectAllHandlerExceptionResolvers(true);
        dispatcherServlet.setDetectAllHandlerMappings(true);
        dispatcherServlet.setDetectAllViewResolvers(true);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        String active = System.getProperty("spring.profiles.active", "dev");

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("config/application-" + active + ".yml"));

        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(Objects.requireNonNull(yaml.getObject()));
        return propertySourcesPlaceholderConfigurer;
    }

    /**
     * multipart upload
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(256));
        factory.setMaxRequestSize(DataSize.ofMegabytes(257));
        return factory.createMultipartConfig();
    }

    /**
     * Interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticatedInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/alb-check")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/valid")
                .excludePathPatterns("/user/find-id")
                .excludePathPatterns("/user/find-password")
                .excludePathPatterns("/user/check/duplicate-id")
                .excludePathPatterns("/upload/image")
                .excludePathPatterns("/test")
        ;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
        ;
    }
}
