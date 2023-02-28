package uz.sh;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Shoxruh Bekpulatov
 * Time : 07/02/23
 */
public interface FileStorageService {

    DataDTO<List<Uploads>> loadFile( List<MultipartFile> files );

    DataDTO<List<Uploads>> loadImageAsCompressed( List<MultipartFile> files );

    DataDTO<InputStreamResource> downloadFileById( Long id );

    DataDTO<InputStreamResource> downloadCompressedImageById( Long id );

    DataDTO<Boolean> deleteFromServerById( Long id );

    DataDTO<Boolean> deleteFromServerById( List<Long> ids );
}
