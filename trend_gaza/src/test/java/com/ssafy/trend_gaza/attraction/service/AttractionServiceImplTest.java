package com.ssafy.trend_gaza.attraction.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.ssafy.trend_gaza.attraction.entity.AttractionInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(
		properties = {
				"spring.config.location=classpath:application.properties"
		},
		classes = {AttractionServiceImpl.class }
)
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"com.ssafy"})
class AttractionServiceImplTest {
	
	@Autowired
	private AttractionService attractionService;

	@Test
	@DisplayName("관광지 조회 테스트")
	void testSearchAttractions() {
		//log.debug("관광지 조회 테스트 시작");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("sido", "32");
		map.put("contentTypeId", "12");
		map.put("keyword", "국립");
		
		List<AttractionInfo> attractionInfo = attractionService.searchAttractions(map);
		assertEquals(15, attractionInfo.size());
		
		//log.debug("관광지 조회 테스트 종료");
	}

}