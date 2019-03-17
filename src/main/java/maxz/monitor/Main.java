package maxz.monitor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
@SpringBootApplication
@EnableSwagger2
@Slf4j
@EnableScheduling
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting...");
        new SpringApplication(Main.class).run(args);
    }

    @Bean
    public ExecutorService createExecutor() {
        ThreadFactory threadFactory = (new ThreadFactoryBuilder())
                .setNameFormat("pool-thread-%d")
                .setPriority(3)
                .setDaemon(true)
                .build();
        return Executors.newCachedThreadPool(threadFactory);
    }
}
