package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Category;
import com.home.whattoeat.dto.category.CategoryFindResponse;
import com.home.whattoeat.dto.category.CategorySaveRequest;
import com.home.whattoeat.dto.category.CategorySaveResponse;
import com.home.whattoeat.dto.category.CategoryUpdateRequest;
import com.home.whattoeat.exception.category.DuplicateCategoryException;
import com.home.whattoeat.exception.category.NoSuchCategoryException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class CategoryServiceTest extends ServiceTest {

	@InjectMocks
	private CategoryService categoryService;

	@Nested
	@DisplayName("save 메서드는")
	class SaveCase {

		// given
		CategorySaveRequest request = new CategorySaveRequest("햄버거");

		@Test
		@DisplayName("category 생성 성공")
		public void success_save() {

			// when
			when(categoryRepository.existsByName(anyString())).thenReturn(false);
			when(categoryRepository.save(any(Category.class))).thenReturn(category);

			// then
			CategorySaveResponse result = categoryService.save(request);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("햄버거");
		}

		@Test
		@DisplayName("존재하는 카테고리를 등록하려 할때 category 생성 실패")
		public void fail1_save() {

			// when
			when(categoryRepository.existsByName(anyString())).thenReturn(true);

			// then
			assertThatThrownBy(() -> categoryService.save(request))
					.isInstanceOf(DuplicateCategoryException.class)
					.hasMessage("이미 저장된 카테고리입니다.");
		}
	}

	@Nested
	@DisplayName("findOne 메서드는")
	class FindOneCase {

		@Test
		@DisplayName("category 단건 조회 성공")
		public void success_findOne() {

			// when
			when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

			// then
			CategoryFindResponse result = categoryService.findOne(1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("햄버거");
		}

		@Test
		@DisplayName("존재하지 않는 카테고리를 조회할때 category 단건 조회 실패")
		public void fail1_findOne() {

			// when
			when(categoryRepository.findById(-1L)).thenThrow(NoSuchCategoryException.class);

			// then
			assertThatThrownBy(() -> categoryService.findOne(-1L))
					.isInstanceOf(NoSuchCategoryException.class)
					.hasMessage("존재하지 않는 카테고리입니다.");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {

		// given
		CategoryUpdateRequest request = new CategoryUpdateRequest("피자");

		@Test
		@DisplayName("category 수정 성공")
		public void success_update() {

			// when
			when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

			// then
			categoryService.update(request, 1L);
			CategoryFindResponse result = categoryService.findOne(1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("피자");
		}

		@Test
		@DisplayName("존재하지 않은 카테고리를 조회할때 category 수정 실패")
		public void fail1_update() {

			// when
			when(categoryRepository.findById(-1L)).thenThrow(NoSuchCategoryException.class);

			// then
			assertThatThrownBy(() -> categoryService.update(request, -1L))
					.isInstanceOf(NoSuchCategoryException.class)
					.hasMessage("존재하지 않는 카테고리입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("category 삭제 성공")
		public void success_delete() {

			// when
			when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

			// then
			categoryService.delete(1L);

			verify(categoryRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않은 카테고리를 조회할때 category 삭제 실패")
		public void fail1_delete() {

			// when
			when(categoryRepository.findById(-1L)).thenThrow(NoSuchCategoryException.class);

			// then
			assertThatThrownBy(() -> categoryService.delete(-1L))
					.isInstanceOf(NoSuchCategoryException.class)
					.hasMessage("존재하지 않는 카테고리입니다.");
		}
	}
}