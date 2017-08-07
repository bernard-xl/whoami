package xl.application.hr.whoami.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xl.application.hr.whoami.model.Resume;
import xl.application.hr.whoami.model.ResumeDao;

/**
 * The Resume API.
 */
@RestController
@RequestMapping("/api/v1/resume")
class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    private static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ADMIN");

    private final ResumeDao dao;

    public ResumeController(ResumeDao dao) {
        this.dao = dao;
    }

    /**
     * Resume search, "ADMIN" role is required to perform search on all fields.
     */
    @GetMapping
    public ResumeSearchResult search(@RequestParam("q") String query) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(ROLE_ADMIN)) {
            logger.debug("unauthenticated search with query '{}'", query);
            return new ResumeSearchResult(dao.findById(query));
         }
        logger.debug("authenticated search with query '{}'", query);
        return new ResumeSearchResult(dao.searchByAllFields(query));
    }

    /**
     * Resume submission, all fields must not be null.
     */
    @PostMapping
    public ResumeSubmitResult submit(@Validated @RequestBody Resume resume) {
        Resume persisted = dao.persist(resume);
        logger.debug("resume persisted with id '{}'", persisted.getId());
        return new ResumeSubmitResult(persisted.getId());
    }
}
