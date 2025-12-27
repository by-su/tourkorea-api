package info.tourkorea.articleservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseEntity {

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    @CreatedBy
    private Long createdBy;
    @LastModifiedBy
    private Long updatedBy;

    @Column(nullable = false)
    private Boolean deleted = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    public void setDeletedTrue() {
        this.deleted = true;
    }
    public void setDeletedFalse() {
        this.deleted = false;
    }

    public Boolean isDeleted() {
        return this.deleted;
    }

}
