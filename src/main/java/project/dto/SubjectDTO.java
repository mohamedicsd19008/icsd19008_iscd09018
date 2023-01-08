package project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;


public class SubjectDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private String status;

    private String fatherSubject;

    private List<String> childSubjects = new ArrayList<>();

    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFatherSubject() {
        return fatherSubject;
    }

    public void setFatherSubject(String fatherSubject) {
        this.fatherSubject = fatherSubject;
    }

    public List<String> getChildSubjects() {
        return childSubjects;
    }

    public void setChildSubjects(List<String> childSubjects) {
        this.childSubjects = childSubjects;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SubjectDTO that = (SubjectDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(status, that.status)
                && Objects.equals(fatherSubject, that.fatherSubject) && Objects.equals(childSubjects, that.childSubjects)
                && Objects.equals(createdDateTime, that.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, fatherSubject, childSubjects, createdDateTime);
    }

    @Override
    public String toString() {
        return "SubjectDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", fatherSubject='" + fatherSubject + '\'' +
                ", childSubjects=" + childSubjects +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
