package uz.sh;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Shoxruh Bekpulatov
 * Time : 07/02/23
 */
public class FileIOException extends NestedRuntimeException {
    public FileIOException( String msg ) {
        super(msg);
    }
}
