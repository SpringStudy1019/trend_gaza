package com.ssafy.trend_gaza.attraction.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.trend_gaza.attraction.dto.AttractionAdminRequest;
import com.ssafy.trend_gaza.attraction.dto.AttractionAutoSearchResponse;
import com.ssafy.trend_gaza.attraction.dto.AttractionDetailResponse;
import com.ssafy.trend_gaza.attraction.entity.AttractionInfo;
import com.ssafy.trend_gaza.util.TrieAlgorithmUtil.Node;

public interface AttractionService {
	List<AttractionInfo> searchAttractions(Map<String, String> param);
	void registerAdminAttraction(AttractionAdminRequest attractionAdminRequest);
	String uploadAttractionImage(MultipartFile multipartFile, String realPath) throws Exception;
	AttractionDetailResponse findAttraction(int id);
	Node search(String str);
	List<AttractionAutoSearchResponse> autoComplete(String str, Node node);
	void dfs(Node node, String str);
	int getMaxSize(int size);
	List<AttractionAutoSearchResponse> attractionNameList();
}
