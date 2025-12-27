package info.tourkorea.userservicedomain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    private boolean deleted;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    protected void setDeleted() {
        this.deleted = true;
    }

    protected void setDeletedTrue() {
        this.deleted = true;
    }

    protected  void setDeletedFalse() {
        this.deleted = false;
    }

    protected boolean getDeleteFlag() {
        return this.deleted;
    }
}
