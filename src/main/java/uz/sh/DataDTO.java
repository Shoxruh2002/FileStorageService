package uz.sh;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDTO<T> implements Serializable {

    private T data;

    private AppErrorDto error;

    private boolean success;

    private Long page;

    private Long totalCount; //all elements' count

    private Long count; //current elements' count (not all of them)

    private MediaType mediaType;

    private String fileOriginalName;

    public DataDTO( boolean success ) {
        this.success = success;
    }

    public DataDTO( T data ) {
        this.data = data;
        this.success = true;
    }

    public DataDTO( AppErrorDto error ) {
        this.error = error;
        this.success = false;
    }

    public DataDTO( T data, Long totalCount ) {
        this.data = data;
        this.success = true;
        this.totalCount = totalCount;
    }

    public DataDTO( T data, MediaType mediaType, String fileOriginalName ) {
        this.data = data;
        this.mediaType = mediaType;
        this.fileOriginalName = fileOriginalName;
    }

    public DataDTO( T data, Page page ) {
        this.data = data;
        this.success = true;
        this.totalCount = page.getTotalElements();
        this.count = (long) page.getNumberOfElements();
        this.page = (long) page.getNumber();
    }

}
