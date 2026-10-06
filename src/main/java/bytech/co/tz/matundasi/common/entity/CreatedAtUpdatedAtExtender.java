package bytech.co.tz.matundasi.common.entity;

import jakarta.persistence.*;
import bytech.co.tz.matundasi.common.security.CurrentAuditor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class CreatedAtUpdatedAtExtender {
    @Column(name = "reference_id", unique = true, nullable = false, updatable = false)
    private UUID referenceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "created_by", length = 128, updatable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 128)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        if (referenceId == null) {
            referenceId = UUID.randomUUID();
        }
        createdAt = Instant.now();
        updatedAt = Instant.now();
        createdBy = CurrentAuditor.userId();
        updatedBy = createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
        updatedBy = CurrentAuditor.userId();
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
