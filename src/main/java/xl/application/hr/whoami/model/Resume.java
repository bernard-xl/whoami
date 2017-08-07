package xl.application.hr.whoami.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Resume entity.
 */
public class Resume {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String title;

    @NotNull
    private String company;

    @NotNull
    private String description;

    public Resume() {
    }

    @JsonCreator
    public Resume(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("title") String title,
            @JsonProperty("company") String company,
            @JsonProperty("description") String description) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.company = company;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(id, resume.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
