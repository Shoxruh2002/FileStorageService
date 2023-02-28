package uz.sh;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.dir")
@Getter
@Setter
public class FileStorageProperties {

    private String fileUploadsUrl;

    private String compressedImagesUrl;


}
