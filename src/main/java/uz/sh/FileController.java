package uz.sh;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Shoxruh Bekpulatov
 * Time : 07/02/23
 */
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController( FileStorageService fileStorageService ) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/load/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataDTO<List<Uploads>>> loadFile( @RequestPart List<MultipartFile> files ) {
        DataDTO<List<Uploads>> dataDTO = fileStorageService.loadFile(files);
        return new ResponseEntity<>(dataDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/load/image-as-compressed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataDTO<List<Uploads>>> loadImageAsCompressed( @RequestParam List<MultipartFile> files ) {
        DataDTO<List<Uploads>> dataDTO = fileStorageService.loadImageAsCompressed(files);
        return new ResponseEntity<>(dataDTO, HttpStatus.OK);
    }

    @GetMapping("/download/file-by-id/{id}")
    public ResponseEntity<InputStreamResource> downloadFileById( @PathVariable("id") Long id ) {
        DataDTO<InputStreamResource> dataDTO = fileStorageService.downloadFileById(id);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dataDTO.getFileOriginalName() + "\"")
                .contentType(dataDTO.getMediaType())
                .body(dataDTO.getData());
    }

    @GetMapping("/download/compressed-image-by-id/{id}")
    public ResponseEntity<InputStreamResource> downloadCompressedImageById( @PathVariable("id") Long id ) {
        DataDTO<InputStreamResource> dataDTO = fileStorageService.downloadCompressedImageById(id);
        return ResponseEntity
                .ok()
                .contentType(dataDTO.getMediaType())
                .body(dataDTO.getData());
    }

}
