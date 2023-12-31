package com.ssafy.trend_gaza.review.repository;


import com.ssafy.trend_gaza.attraction.entity.AttractionInfo;
import com.ssafy.trend_gaza.review.dto.ReviewModifyResponse;
import com.ssafy.trend_gaza.review.dto.ScoreCountResponse;
import com.ssafy.trend_gaza.review.entity.FileInfo;
import com.ssafy.trend_gaza.review.entity.Review;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper {

    /* 리뷰 등록 */
    void register(Review registerRequest) throws SQLException;

    /* 이미지 업로드 */
    void registerFile(Review registerRequest) throws Exception;

    /* 리뷰 전체 조회 (+검색) */
    List<Review> list(Map<String, Object> param) throws SQLException;

    /* 리뷰 카운트 */
    int getTotalReviewCount(Map<String, Object> param) throws SQLException;

    /* 리뷰 상세 조회 */
    Review view(int reviewIdx) throws SQLException;

    /* 리뷰 이미지 조회 */
    List<FileInfo> getReviewImage(int reviewIdx);

    /* 리뷰 수정 */
    Review getModify(int reviewIdx) throws SQLException;

    void modify(ReviewModifyResponse modifyResponse) throws SQLException;

    /* 리뷰 삭제 */
    void delete(int reviewIdx) throws SQLException;

    /* 사용자들이 가장 많이 리뷰를 남긴 여행지 조회 */
    List<AttractionInfo> mostReviews() throws SQLException;

    /* 여행지별 점수 카운트 */
    ScoreCountResponse countScores(int contentId) throws SQLException;

    /* contentId별 리뷰들 */
    List<Review> getReviewsByContentId(int contentId) throws SQLException;

    /* 사용자의 아이디로 리뷰 가져오기  */
    List<Review> getReviewsByUserId(String userId) throws SQLException;
}
