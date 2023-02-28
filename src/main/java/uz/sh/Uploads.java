package uz.sh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Uploads extends Auditable {

    private String originalName;
    private String generatedName;
    private String contentType;
    private long size;
    private String path;
    private boolean isCompressed;
}
