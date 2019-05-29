package pl.konradboniecki.budget.budgetmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import pl.konradboniecki.chassis.configuration.HttpLoggingAutoConfiguration;

@SpringBootApplication
@Import(HttpLoggingAutoConfiguration.class)
public class BudgetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetManagementApplication.class, args);
    }

    @Autowired
    private CommonsRequestLoggingFilter commonsRequestLoggingFilter;

    @Bean
    public FilterRegistrationBean loggingFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(commonsRequestLoggingFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

}
