package com.ssafy.trend_gaza.plan.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.trend_gaza.plan.dto.PlanRequest;
import com.ssafy.trend_gaza.plan.service.PlanService;

@Controller
@RequestMapping("/plans")
public class PlanController {
	
	private final PlanService planService;
	
	public PlanController(PlanService planService) {
		this.planService = planService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAttractions() {
		return ResponseEntity.ok("");
	}
	
	@PostMapping("/{planIdx}")
	public ResponseEntity<?> registerPlan(@PathVariable int planIdx,
			@RequestBody List<PlanRequest> planRequest) {
		planService.registerPlan(planRequest, planIdx);
		return ResponseEntity.created(URI.create("")).build();		// URI 경로 지정 필요함
	}

}