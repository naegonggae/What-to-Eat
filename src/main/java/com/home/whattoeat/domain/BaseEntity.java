package com.home.whattoeat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseEntity {

	@Column(updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		LocalDateTime now = LocalDateTime.now();
		updatedAt = now;
	}

	public void softDelete() {
		LocalDateTime now = LocalDateTime.now();
		deletedAt = now;
	}

}
