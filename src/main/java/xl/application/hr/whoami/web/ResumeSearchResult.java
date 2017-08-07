package xl.application.hr.whoami.web;

import xl.application.hr.whoami.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Resume Search Result DTO.
 */
class ResumeSearchResult {

    private final List<Resume> found;

    public ResumeSearchResult(Optional<Resume> found) {
        this.found = found
                .map(r -> Collections.singletonList(r))
                .orElseGet(() -> Collections.emptyList());
    }

    public ResumeSearchResult(List<Resume> found) {
        this.found = found;
    }

    public List<Resume> getFound() {
        return found;
    }
}
