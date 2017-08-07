package xl.application.hr.whoami.model;

import java.util.List;
import java.util.Optional;

/**
 * Access {@link Resume} entities from data store.
 */
public interface ResumeDao {

    Resume persist(Resume resume);

    Optional<Resume> findById(String id);

    List<Resume> searchByAllFields(String query);
}
