package ru.spb.n31.n31_by_matthew_java_legend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.spb.n31.n31_by_matthew_java_legend.util.AdminProperties;
import ru.spb.n31.n31_by_matthew_java_legend.util.JwtProperties;

@EnableConfigurationProperties(value = {AdminProperties.class, JwtProperties.class})
@SpringBootApplication
public class N31ByMatthewJavaLegendApplication {

    public static void main(String[] args) {
        SpringApplication.run(N31ByMatthewJavaLegendApplication.class, args);
    }

}
