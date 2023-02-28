package uz.sh;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Botirov Najmiddin, Mon 12:25 PM. 6/20/2022
 */
public interface UploadsRepository extends JpaRepository<Uploads, Long> {

    Optional<Uploads> findByGeneratedName(String filename);

    Optional<Uploads> findByIdAndDeletedFalse( Long id );

}
