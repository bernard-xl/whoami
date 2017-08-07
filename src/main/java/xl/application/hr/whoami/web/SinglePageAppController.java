package xl.application.hr.whoami.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class SinglePageAppController {

    /**
     * Serve index.html to multiple URLs, workaround the client-side routing issue.
     */
    @GetMapping({"", "search"})
    public String applicationPage() {
        return "index";
    }
}
