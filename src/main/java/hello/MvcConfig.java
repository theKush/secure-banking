package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/balance").setViewName("balance");
        registry.addViewController("/transfer").setViewName("transfer");
        registry.addViewController("/profile").setViewName("profile");
        registry.addViewController("/checkImage").setViewName("checkImage");
        registry.addViewController("/success").setViewName("success");
        registry.addViewController("/errors").setViewName("errors");
    }

}
