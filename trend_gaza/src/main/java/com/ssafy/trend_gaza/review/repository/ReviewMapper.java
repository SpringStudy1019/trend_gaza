package com.ssafy.trend_gaza.review.repository;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trend_gaza.review.dto.ReviewModifyRequest;
import com.ssafy.trend_gaza.review.dto.ReviewRegisterRequest;
import com.ssafy.trend_gaza.review.entity.Review;

@Mapper
public interface ReviewMapper {

	/* 리뷰 등록 */ 
	void register(ReviewRegisterRequest registerRequest) throws SQLException;
	
	/* 리뷰 전체 조회 (+검색) */
	List<Review> list() throws SQLException;
	
	/* 리뷰 수정 */
	void modify(ReviewModifyRequest ModifyRequest) throws SQLException;
	
	/* 리뷰 삭제 */ 
	void delete(String reviewIdx) throws SQLException;
}