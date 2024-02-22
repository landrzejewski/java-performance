package pl.training.performance.reports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.training.performance.reports.domain.ReportGenerator;
import pl.training.performance.reports.ports.DataProvider;

@EnableJpaRepositories
@Configuration
public class ApplicationConfig {

    @Bean
    public ReportGenerator reportGenerator(DataProvider dataProvider) {
        return new ReportGenerator(dataProvider);
    }

}
