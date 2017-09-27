package com.shark.search4SVN;
import com.shark.search4SVN.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import static com.shark.search4SVN.util.Search4SVNContext.*;


@SpringBootApplication
@EnableConfigurationProperties({SolrConnProperties.class})
public class Search4SVNApplication {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = SpringApplication.run(Search4SVNApplication.class, args);
        Search4SVNContext.setApplicationContext(applicationContext);

    }
}
