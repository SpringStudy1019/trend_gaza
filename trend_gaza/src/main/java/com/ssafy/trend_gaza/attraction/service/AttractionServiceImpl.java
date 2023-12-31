package com.ssafy.trend_gaza.attraction.service;


import com.ssafy.trend_gaza.attraction.dto.AttractionAdminRequest;
import com.ssafy.trend_gaza.attraction.dto.AttractionAutoSearchResponse;
import com.ssafy.trend_gaza.attraction.dto.AttractionBookmarkResponse;
import com.ssafy.trend_gaza.attraction.dto.AttractionCategoryResponse;
import com.ssafy.trend_gaza.attraction.dto.AttractionDetailResponse;
import com.ssafy.trend_gaza.attraction.dto.AttractionPlanResponse;
import com.ssafy.trend_gaza.attraction.dto.AttractionSearchResponse;
import com.ssafy.trend_gaza.attraction.entity.AttractionInfo;
import com.ssafy.trend_gaza.attraction.repository.AttractionMapper;
import com.ssafy.trend_gaza.util.FileUtil;
import com.ssafy.trend_gaza.util.SizeConstant;
import com.ssafy.trend_gaza.util.TrieAlgorithmUtil;
import com.ssafy.trend_gaza.util.TrieAlgorithmUtil.Node;
import java.io.File;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttractionServiceImpl implements AttractionService {

    private final String UPLOAD_PATH = "/upload";

    private final AttractionMapper attractionMapper;
    private final FileUtil fileUtil;
    private final TrieAlgorithmUtil trie;

    public AttractionServiceImpl(
            AttractionMapper attractionMapper, FileUtil fileUtil, TrieAlgorithmUtil trie) {
        super();
        this.attractionMapper = attractionMapper;
        this.fileUtil = fileUtil;
        this.trie = trie;

        List<AttractionAutoSearchResponse> lst = attractionNameList();

        for (AttractionAutoSearchResponse response : lst) {
            trie.insert(response.getContentId(), response.getTitle().replaceAll(" ", ""));
        }
    }

    @Override
    public AttractionSearchResponse searchAttractions(Map<String, String> param) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sido = param.get("sido");
        String contentTypeId = param.get("contentTypeId");
        String keyword = param.get("keyword");

        map.put("sido", sido == null ? "" : sido);
        map.put("contentTypeId", contentTypeId == null ? "" : contentTypeId);
        map.put("keyword", keyword == null ? "" : keyword);

        int pgNo = Integer.parseInt(param.get("pgno") == null ? "1" : param.get("pgno"));
        int currentPage = Integer.parseInt(param.get("pgno") == null ? "1" : param.get("pgno"));
        int sizePerPage = Integer.parseInt(param.get("spp") == null ? "20" : param.get("spp"));

        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
        map.put("start", start);
        map.put("listsize", SizeConstant.LIST_SIZE);

        List<AttractionInfo> list = attractionMapper.searchAttractions(map);
        int totalAttractionCount = attractionMapper.getTotalAttractionCount(map);
        int totalPageCount = (totalAttractionCount - 1) / sizePerPage + 1;

        AttractionSearchResponse attractionSearchResponse =
                AttractionSearchResponse.builder()
                        .attractions(list)
                        .currentPage(currentPage)
                        .totalPageCount(totalPageCount)
                        .build();
        return attractionSearchResponse;
        // return result.stream().map(AttractionResponse::of).toList();
    }

    /**
     * 카테고리별 리스트 조회(searchByCategory)를 이용한 페이지네이션 추가
     *
     * @param map
     * @return
     */
    @Override
    public AttractionCategoryResponse listByCategory(Map<String, String> param) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sido = param.get("sido");
        int contentTypeId = Integer.parseInt(param.get("contentTypeId"));
        String keyword = param.get("keyword");

        map.put("sido", sido == null ? "" : sido);
        map.put("contentTypeId", contentTypeId == 0 ? "" : contentTypeId);
        map.put("keyword", keyword == null ? "" : keyword);

        int pgNo = Integer.parseInt(param.get("pgno") == null ? "1" : param.get("pgno"));
        int currentPage = Integer.parseInt(param.get("pgno") == null ? "1" : param.get("pgno"));
        int sizePerPage = Integer.parseInt(param.get("spp") == null ? "20" : param.get("spp"));

        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
        map.put("start", start);
        map.put("listsize", SizeConstant.LIST_SIZE);

        List<AttractionInfo> list = attractionMapper.searchByCategory(map);
        int totalAttractionCount = attractionMapper.getTotalAttractionCount(map);
        int totalPageCount = (totalAttractionCount - 1) / sizePerPage + 1;

        AttractionCategoryResponse response =
                AttractionCategoryResponse.builder()
                        .attractions(list)
                        .currentPage(currentPage)
                        .totalPageCount(totalPageCount)
                        .build();
        return response;
    }

    @Override
    // @Transactional
    public void registerAdminAttraction(AttractionAdminRequest attractionAdminRequest) {
        attractionMapper.registerAdminAttraction(attractionAdminRequest);
        // AttractionDetail attractionDetail = new AttractionDetail();
        // attractionDetail.setContentId(contentId);
        //		attractionMapper.registerAttractionDetail(attractionAdminRequest);
    }

    @Override
    public String uploadAttractionImage(MultipartFile multipartFile, String realPath)
            throws Exception {
        String fileName = multipartFile.getOriginalFilename();

        String today = fileUtil.getFolderName();
        String saveFolder = fileUtil.getFolder(realPath, today);
        fileUtil.makeFolder(saveFolder);

        return today
                + File.separator
                + fileUtil.upload(fileName, multipartFile, new File(saveFolder));
    }

    @Override
    public AttractionDetailResponse findAttraction(int id) {
        AttractionDetailResponse attractionDetailResponse =
                attractionMapper.getAttractionDetail(id);
        return attractionDetailResponse;
    }

    @Override
    public Node search(String str) {
        return trie.search(str);
    }

    @Override
    public List<AttractionAutoSearchResponse> autoComplete(String str, Node node) {
        List<AttractionAutoSearchResponse> lst = trie.autoComplete(str, node);
        lst.sort(Comparator.comparing(AttractionAutoSearchResponse::getTitle));
        List<AttractionAutoSearchResponse> result = lst.subList(0, getMaxSize(lst.size()));
        return result.stream()
                .map(
                        response ->
                                AttractionAutoSearchResponse.builder()
                                        .contentId(response.getContentId())
                                        .title(response.getTitle())
                                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void dfs(Node node, String str) {
        trie.dfs(node, str);
    }

    @Override
    public int getMaxSize(int size) {
        if (size < 8) {
            return size;
        }
        return 8;
    }

    @Override
    public List<AttractionAutoSearchResponse> attractionNameList() {
        return attractionMapper.attractionNameList();
    }

    /**
     * 카테고리별 리스트 조회
     *
     * @param map
     * @return
     */
    @Override
    public List<AttractionInfo> searchByCategory(Map<String, String> param) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sido = param.get("sido");
        String contentTypeId = param.get("contentTypeId");
        String keyword = param.get("keyword");

        map.put("sido", sido == null ? "" : sido);
        map.put("contentTypeId", contentTypeId == null ? "" : contentTypeId);
        map.put("keyword", keyword == null ? "" : keyword);

        List<AttractionInfo> list = attractionMapper.searchByCategory(map);

        return list;
    }

    /*
     * 여행 계획 세울 때 보이는 여행지 리스트
     */
    @Override
    public List<AttractionPlanResponse> getAttractionPlanResponse(Map<String, String> map) {

        Map<String, Object> param = new HashMap<String, Object>();

        int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
        param.put("start", start);
        param.put("listsize", SizeConstant.LIST_SIZE);

        return attractionMapper.attractionPlan(param);
    }

    @Override
    public int onBookmark(int attractionId, String userId) throws SQLException {
        return 0;
    }

    @Override
    public AttractionDetailResponse getAttractionDetail(int id) {
        AttractionDetailResponse attractionDetailResponse =
                attractionMapper.getAttractionDetail(id);
        return attractionDetailResponse;
    }

    @Override
    public List<AttractionInfo> searchByword(String keyword) {
        return attractionMapper.searchByword(keyword);
    }

    @Override
    public List<Map<String, String>> getUserImage(int contentId) throws Exception {
        return attractionMapper.getUserImage(contentId);
    }

    @Override
    public List<AttractionBookmarkResponse> findBookmarkAndAttractionByUserIds(int planIdx) {
        return attractionMapper.findBookmarkAndAttractionByUserIds(planIdx);
    }

    //	@Override
    //	public int onBookmark(int contentId, String userId) throws SQLException {
    //		int result = 0;
    //		BookmarkRequest bookmarkRequest = BookmarkRequest.builder()
    //				.userId(userId)
    //				.contentId(contentId)
    //				.build();
    //
    //		Bookmark bookmark = attractionMapper.findBookmark(bookmarkRequest);
    //
    //		if(bookmark == null) {
    //			result = attractionMapper.onBookmark(bookmarkRequest);
    //		}
    //
    //		return result;
    //	}

}
