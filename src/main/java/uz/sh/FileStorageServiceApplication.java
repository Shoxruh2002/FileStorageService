package uz.sh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {
        FileStorageProperties.class
})
@SpringBootApplication
public class FileStorageServiceApplication {

    public static void main( String[] args ) {
        SpringApplication.run(FileStorageServiceApplication.class, args);
    }

}
