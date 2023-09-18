package com.home.whattoeat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Where;

@MappedSuperclass
@Getter @ToString
@Where(clause = "deleted_at is NULL") // 엔티티를 조회할때 deletedAt이 Null 인것만 가져옴 == 삭제안된거
// 이거 지금 제대로 기능안하고 있음 상속받은 엔티티에 직접 붙여야 기능을 함 상속과정에서 문제가 있는걸까?
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
