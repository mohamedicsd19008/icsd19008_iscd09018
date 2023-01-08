package project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class NewsDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private String status;

    @NotNull(message = "must not be null")
    private String content;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdDateTime;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime publicationDateTime;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private List<Long> comments = new ArrayList<>();

    @NotEmpty
    @NotNull
    private List<String> subjects = new ArrayList<>();

    @Schema(accessMode = AccessMode.READ_ONLY)
    private String rejectionReason;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getPublicationDateTime() {
        return publicationDateTime;
    }

    public void setPublicationDateTime(LocalDateTime publicationDateTime) {
        this.publicationDateTime = publicationDateTime;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NewsDTO newsDTO = (NewsDTO) o;
        return Objects.equals(id, newsDTO.id) && Objects.equals(title, newsDTO.title) && Objects.equals(status, newsDTO.status)
                && Objects.equals(content, newsDTO.content) && Objects.equals(createdDateTime, newsDTO.createdDateTime)
                && Objects.equals(publicationDateTime, newsDTO.publicationDateTime) && Objects.equals(comments, newsDTO.comments)
                && Objects.equals(subjects, newsDTO.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, content, createdDateTime, publicationDateTime, comments, subjects);
    }
}
