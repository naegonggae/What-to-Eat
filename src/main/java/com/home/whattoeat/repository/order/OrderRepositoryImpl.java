package com.home.whattoeat.repository.order;

import static com.home.whattoeat.domain.QMember.member;
import static com.home.whattoeat.domain.QOrder.order;
import static com.home.whattoeat.domain.QRestaurant.restaurant;

import com.home.whattoeat.domain.Order;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

// 클래스 이름은 무조건 OrderRepository + Impl 해야함 -> 규칙임
public class OrderRepositoryImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public OrderRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	// 내 주문리스트 전체 조회
	@Override
	public Page<Order> findAllOrder(String username, Pageable pageable) {

		if (username.isEmpty()) throw new NoSuchMemberException();

		pageable = PageRequest.of(0, 3);

		List<Order> content = queryFactory
				.selectFrom(order)
				.join(order.member, member).fetchJoin()
				.join(order.restaurant, restaurant).fetchJoin()
				.where(member.username.eq(username))
				.orderBy(order.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(order.createdAt.count())
				.from(order)
				.join(order.member, member).fetchJoin()
				.join(order.restaurant, restaurant).fetchJoin()
				.where(member.username.eq(username));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

}
