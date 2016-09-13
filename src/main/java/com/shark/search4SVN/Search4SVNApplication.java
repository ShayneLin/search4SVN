package com.shark.search4SVN;
import com.shark.search4SVN.util.Search4SVNContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class Search4SVNApplication {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = SpringApplication.run(Search4SVNApplication.class, args);
        Search4SVNContext.setApplicationContext(applicationContext);
    }
}
