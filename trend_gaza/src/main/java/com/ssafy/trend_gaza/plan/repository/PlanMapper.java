package com.ssafy.trend_gaza.plan.repository;


import com.ssafy.trend_gaza.plan.dto.AcceptInvitationRequest;
import com.ssafy.trend_gaza.plan.dto.PlanDetailResponse;
import com.ssafy.trend_gaza.plan.dto.PlanGetModifyResponse;
import com.ssafy.trend_gaza.plan.dto.PlanResponse;
import com.ssafy.trend_gaza.plan.dto.SelectAttractionPlanResponse;
import com.ssafy.trend_gaza.plan.dto.SetPlanRequest;
import com.ssafy.trend_gaza.plan.entity.Plan;
import com.ssafy.trend_gaza.user.entity.User;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

@Mapper
public interface PlanMapper {
    int registerPlan(Map<String, Object> param);

    int modifySelectPlan(Map<String, Object> param);

    Plan IsPlanOwner(Map<String, Object> param);

    int deletePlan(Map<String, Object> param);

    int deleteMyPlan(Map<String, Object> param);

    List<PlanResponse> getMyPlans(String userId);

    /* 알림을 받은 사용자가 여행 계획을 수락하면, 그 여행에 참여하게 된다 */
    int joinPlan(Map<String, Object> param);

    /* 여행 계획표 (방) 만들기 */
    int setPlan(@RequestBody SetPlanRequest setPlanRequest);

    /* 초대된 여행 조회하기 */
    List<Plan> getInvitedPlans(String userId);

    /* 초대 수락할 여행 1개 조회하기 */
    int getInvitedPlan(AcceptInvitationRequest acceptInvitationRequest);

    /* 내가 만든 여행 조회하기 */
    List<PlanResponse> getCreatedPlans(String userId);

    /* 여행 일정 상세보기 */
    PlanDetailResponse getPlanDetail(int planIdx);

    /* 여행 계획 조회 */
    List<SelectAttractionPlanResponse> getSelectAttractionPlan(int attractionPlanId);

    PlanGetModifyResponse getModifyPlan(int planIdx);

    int modifyPlan(Map<String, Object> map);

    List<User> getParticipant(int planIdx);

    Integer getAttractionPlanId(int planIdx);
}
