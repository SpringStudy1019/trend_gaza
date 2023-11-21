package com.ssafy.trend_gaza.plan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trend_gaza.plan.dto.AcceptInvitationRequest;
import com.ssafy.trend_gaza.plan.dto.SelectAttractionPlanResponse;
import com.ssafy.trend_gaza.plan.dto.PlanDetailResponse;
import com.ssafy.trend_gaza.plan.dto.PlanRequest;
import com.ssafy.trend_gaza.plan.dto.PlanResponse;
import com.ssafy.trend_gaza.plan.dto.SetPlanRequest;
import com.ssafy.trend_gaza.plan.entity.Plan;
import com.ssafy.trend_gaza.plan.repository.PlanMapper;

@Service
public class PlanServiceImpl implements PlanService {

	private final PlanMapper planMapper;
	
	public PlanServiceImpl(PlanMapper planMapper) {
		this.planMapper = planMapper;
	}
	
	@Override
	public int registerPlan(List<PlanRequest> planRequest, int planIdx) {
		int result = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(planRequest);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("attraction", json);
			param.put("planIdx", planIdx);
			result = planMapper.registerPlan(param);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int modifyPlan(List<PlanRequest> planRequest, int attractionPlanId) {
		int result = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(planRequest);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("attraction", json);
			param.put("attractionPlanId", attractionPlanId);
			result = planMapper.modifyPlan(param);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
  
	/*
	 * 여행 계획을 세운 사람이 탈퇴하면 관련된 데이터들을 모두 지운다.
	 */
	@Override
	@Transactional
	public int deletePlan(int planIdx, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("planIdx", planIdx);
		param.put("userId", userId);
		if(planMapper.IsPlanOwner(param) != null) {
			return planMapper.deletePlan(param);
		}
		return planMapper.deleteMyPlan(param);
  }
  
	@Override
	public List<PlanResponse> getMyPlans(String userId) {
		return planMapper.getMyPlans(userId);
	}

	@Override
	public int joinPlan(int planIdx, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("planIdx", planIdx);
		param.put("userId", userId);
		return planMapper.joinPlan(param);
	}

	@Override
	public int setPlan(SetPlanRequest setPlanRequest) {
		return planMapper.setPlan(setPlanRequest);
	}

	@Override
	public List<Plan> getInvitedPlans(String userId) {
		return planMapper.getInvitedPlans(userId);
	}

	@Override
	public int getInvitedPlan(AcceptInvitationRequest acceptInvitationRequest) {
		return planMapper.getInvitedPlan(acceptInvitationRequest);
	}

	@Override
	public List<PlanResponse> getCreatedPlans(String userId) {
		return planMapper.getCreatedPlans(userId);
	}

	@Override
	public PlanDetailResponse getPlanDetail(int planIdx) {
		return planMapper.getPlanDetail(planIdx);
	}

	@Override
	public List<SelectAttractionPlanResponse> getSelectAttractionPlan(int attractionPlanId) {
		return planMapper.getSelectAttractionPlan(attractionPlanId);
	}
	
	

}
