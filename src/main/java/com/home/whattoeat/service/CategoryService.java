package com.home.whattoeat.service;

import com.home.whattoeat.domain.Category;
import com.home.whattoeat.dto.category.CategoryFindResponse;
import com.home.whattoeat.dto.category.CategorySaveRequest;
import com.home.whattoeat.dto.category.CategorySaveResponse;
import com.home.whattoeat.dto.category.CategoryUpdateRequest;
import com.home.whattoeat.exception.category.DuplicateCategoryException;
import com.home.whattoeat.exception.category.NoSuchCategoryException;
import com.home.whattoeat.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	// 생성
	@Transactional
	public CategorySaveResponse save(CategorySaveRequest request) {
		// 개발팀만 카테고리를 생성 할 수 있다

		boolean existsByName = categoryRepository.existsByName(request.getCategoryName());
		if (existsByName) throw new DuplicateCategoryException();

		Category category = Category.createCategory(request.getCategoryName());

		System.out.println("===");
		System.out.println(request.getCategoryName());
		System.out.println("category = " + category.getName());

		Category savedCategory = categoryRepository.save(category);

		return CategorySaveResponse.from(savedCategory);
	}

	// 단건조회
	public CategoryFindResponse findOne(Long id) {

		Category findCategory = categoryRepository.findById(id)
				.orElseThrow(NoSuchCategoryException::new);

		return CategoryFindResponse.from(findCategory.getId());
	}

	// 수정
	@Transactional
	public void update(CategoryUpdateRequest request, Long id) {
		Category findCategory = categoryRepository.findById(id)
				.orElseThrow(NoSuchCategoryException::new);
		// 식당이름 중복확인 로직 추가

		findCategory.update(request);
	}

	// 삭제
	@Transactional
	public void delete(Long id) {
		Category findCategory = categoryRepository.findById(id)
				.orElseThrow(NoSuchCategoryException::new);

		// 카테고리를 사용하는 곳이 있으면 카테고리 리스트에서 다 제외 시켜주기
		findCategory.softDelete();
	}

}