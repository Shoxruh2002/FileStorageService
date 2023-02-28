package uz.sh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Shoxruh Bekpulatov
 * Time : 07/02/23
 */
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final UploadsRepository uploadsRepository;
    private final FileStorageProperties fileStorageProperties;

    public FileStorageServiceImpl( UploadsRepository uploadsRepository, FileStorageProperties fileStorageProperties ) {
        this.uploadsRepository = uploadsRepository;
        this.fileStorageProperties = fileStorageProperties;
    }


    /**
     * .jpg , .jpeg  , .png formatdagi file larni compress qiladi va compressed directory ga store qiladi;
     * Compress qilib yaratilgan file name original fileNamega "-compressed" quwiw orqali generated qilinadi
     * file parametrlari buyica Uploads.java generatriya qilib return qiladi
     *
     * @param file
     * @return DataDto.class
     */

    public DataDTO<Uploads> loadImageAsCompressed( MultipartFile file ) {
        try {
            String contentType = file.getContentType();
            long size = file.getSize();
            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String extension = split[split.length - 1];
            String generatedName = UUID.randomUUID() + "." + extension;

            InputStream input = file.getInputStream();
            BufferedImage image = ImageIO.read(input);

            File compressedImageFile = new File(fileStorageProperties.getCompressedImagesUrl() + File.separator + generatedName);
            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
            ImageWriter writer = (ImageWriter) writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.2f);
            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();
            Uploads uploads = new Uploads(originalFilename, generatedName, contentType, size, fileStorageProperties.getCompressedImagesUrl(), true);
            Uploads savedUploads = uploadsRepository.save(uploads);
            return new DataDTO<>(savedUploads);
        } catch ( IOException e ) {
            log.error("Exception occurred while compressing and saving file: Method : {}; Cause : {}", "compressAndSaveImage", e.getMessage());
            throw new FileIOException(ErrorMessages.EXCEPTION_OCCURRED_WHILE_COMPRESSING_AND_SAVING_FILE_WITH_NAME + file.getOriginalFilename());
        }

    }

    /**
     * File qabul qilib uni rasm ekanligiga tekshiradi.
     * Compress qilisdan oldin tekwiriw kk
     *
     * @param file
     * @return Boolean.class
     */
    private void validateFileForCompressible( MultipartFile file ) {
        String contentType = file.getContentType();
        if ( !( Objects.nonNull(contentType) &&
                ( contentType.equals(MediaType.IMAGE_JPEG_VALUE)
                        || contentType.equals(MediaType.IMAGE_PNG_VALUE)
                        || contentType.equals("image/jpg") ) ) )
            throw new BadRequestException(ErrorMessages.UNCOMPRESSIBLE_FILE_LOADED_WITH_NAME + file.getOriginalFilename());
    }

    /**
     * luboyformatdagi file ni id siga kora uploads dan topadi.
     * uploads dagi parametrlarga mos fileni files directory dan topadi va return qiladi.
     * Bunda DataDto icida contentType ham junatiladi
     *
     * @param id
     * @return DataDto.class
     */


    @Override
    public DataDTO<InputStreamResource> downloadFileById( Long id ) {
        Optional<Uploads> byId = uploadsRepository.findByIdAndDeletedFalse(id);
        if ( byId.isPresent() ) {
            Uploads uploads = byId.get();
            try {
                FileInputStream fileInputStream = new FileInputStream(fileStorageProperties.getFileUploadsUrl()
                        + File.separator + uploads.getGeneratedName());
                return new DataDTO<>(new InputStreamResource(fileInputStream), MediaType.parseMediaType(uploads.getContentType()), uploads.getOriginalName());
            } catch ( IOException e ) {
                log.error("Exception occurred while downloading file: id : {}; Method : {}; Cause : {}", id, "download", e.getMessage());
                throw new FileIOException(ErrorMessages.EXCEPTION_OCCURRED_DOWNLOADING_FILE_WITH_ID + id);
            }
        } else
            throw new NotFoundException("File not found with this id : " + id);
    }


    /**
     * luboyformatdagi file larni listini  files directory ga store qiladi;
     * file parametrlari buyica Uploads.java generatriya qilib  uploadslarni listini return qiladi
     *
     * @param files
     * @return DataDto.class
     */
    @Override
    public DataDTO<List<Uploads>> loadFile( List<MultipartFile> files ) {
        List<Uploads> uploads = files.stream().map(this::loadToSystem).collect(Collectors.toList());
        return new DataDTO<>(uploads);
    }

    /**
     * List qabul qilib save qiladi
     * .jpg , .jpeg  , .png formatdagi file larni compress qiladi va compressed directory ga store qiladi;
     * Compress qilib yaratilgan file name original fileNamega "-compressed" quwiw orqali generated qilinadi
     * file parametrlari buyica Uploads.java generatriya qilib return qiladi
     *
     * @param files
     * @return DataDto.class
     */

    @Override
    public DataDTO<List<Uploads>> loadImageAsCompressed( List<MultipartFile> files ) {
        List<Uploads> uploads = new ArrayList<>();
        for ( MultipartFile file : files ) {         //streamga utkazilmasin
            this.validateFileForCompressible(file);
            DataDTO<Uploads> dataDTO = this.loadImageAsCompressed(file);
            uploads.add(dataDTO.getData());
        }
        return new DataDTO<>(uploads);
    }


    /**
     * .jpg , .jpeg  , .png formatdagi file ni id siga kora uploads dan topadi.
     * uploads dagi parametrlarga mos fileni compressed directory dan topadi va return qiladi.
     * Bunda DataDto icida contentType ham junatiladi
     *
     * @param id
     * @return DataDto.class
     */
    @Override
    public DataDTO<InputStreamResource> downloadCompressedImageById( Long id ) {
        Optional<Uploads> byId = uploadsRepository.findByIdAndDeletedFalse(id);
        if ( byId.isPresent() ) {
            Uploads uploads = byId.get();
            try {
                String url = fileStorageProperties.getCompressedImagesUrl()
                        + File.separator + uploads.getGeneratedName();
                FileInputStream fileInputStream = new FileInputStream(url);
                return new DataDTO<>(new InputStreamResource(fileInputStream), MediaType.parseMediaType(uploads.getContentType()), uploads.getOriginalName());
            } catch ( IOException e ) {
                log.error("Exception occurred while downloading file: id : {}; Method : {}; Cause : {}", id, "download", e.getMessage());
                throw new FileIOException(ErrorMessages.EXCEPTION_OCCURRED_DOWNLOADING_FILE_WITH_ID + id);
            }
        } else
            throw new NotFoundException(ErrorMessages.ITEM_NOT_FOUND_WITH_THIS_ID + id);
    }


    @Override
    public DataDTO<Boolean> deleteFromServerById( Long id ) {
        Optional<Uploads> optionalUploads = uploadsRepository.findByIdAndDeletedFalse(id);
        if ( optionalUploads.isPresent() ) {
            Uploads uploads = optionalUploads.get();
            uploads.setDeleted(true);
            uploadsRepository.save(uploads);
            deleteFromSystemByName(uploads.getGeneratedName(), uploads.isCompressed());
        }
        return new DataDTO<>(true);
    }

    @Override
    public DataDTO<Boolean> deleteFromServerById( List<Long> ids ) {
        ids.forEach(this::deleteFromServerById);
        return new DataDTO<>(true);
    }

    private void deleteFromSystemByName( String name, boolean isCompressed ) {
        try {
            if ( isCompressed )
                Files.delete(Path.of(fileStorageProperties.getCompressedImagesUrl() + File.separator + name));
            else Files.delete(Path.of(fileStorageProperties.getFileUploadsUrl() + File.separator + name));
        } catch ( IOException e ) {
            log.error("Exception Occurred while deleting file with name : {}; Cause :{}", name, e.getMessage());
            throw new FileIOException(ErrorMessages.EXCEPTION_OCCURRED_WHILE_DELETING_FILE_WITH_NAME + name);
        }
    }


    private Uploads loadToSystem( MultipartFile file ) {
        try {
            long size = file.getSize();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            if ( Objects.isNull(contentType) || contentType.isBlank() )
                contentType = MediaType.MULTIPART_FORM_DATA_VALUE;
            String[] split = originalFilename.split("\\.");
            String generatedName = UUID.randomUUID() + "." + split[split.length - 1];

            Uploads uploads = new Uploads(originalFilename, generatedName, contentType, size, fileStorageProperties.getFileUploadsUrl(), false);
            Uploads save = uploadsRepository.save(uploads);
            Path path = Paths.get(fileStorageProperties.getFileUploadsUrl()
                    + File.separator + generatedName);
            Files.copy(file.getInputStream(), path);
            return save;
        } catch ( IOException e ) {
            log.error("Exception occurred while saving file: Method : {}; Cause : {}", "load", e.getMessage());
            throw new FileIOException(ErrorMessages.EXCEPTION_OCCURRED_WHILE_SAVING_FILE_WITH_NAME + file.getOriginalFilename());
        }
    }
}
