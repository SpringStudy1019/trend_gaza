package com.ssafy.trend_gaza.review.controller;


import com.ssafy.trend_gaza.review.dto.ReviewModifyResponse;
import com.ssafy.trend_gaza.review.dto.ReviewRegisterRequest;
import com.ssafy.trend_gaza.review.dto.ReviewResponse;
import com.ssafy.trend_gaza.review.entity.Review;
import com.ssafy.trend_gaza.review.service.ReviewService;
import com.ssafy.trend_gaza.util.AuthenticationUtil;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@CrossOrigin("*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        super();
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody ReviewRegisterRequest reviewRegisterRequest) {
        try {
            reviewRegisterRequest.setUserId(AuthenticationUtil.getCurrentUserSocialId());
            reviewService.register(reviewRegisterRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam Map<String, String> map) {
        try {
            ReviewResponse reviewResponse = reviewService.list(map);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            return ResponseEntity.ok().headers(header).body(reviewResponse);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping(value = "/view/{reviewIdx}")
    public ResponseEntity<?> view(@PathVariable int reviewIdx) {
        try {
            Review review = reviewService.view(reviewIdx);
            if (review != null) {
                return new ResponseEntity<Review>(review, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping("/{reviewIdx}/images")
    public ResponseEntity<?> getReviewImages(@PathVariable int reviewIdx) {
        return ResponseEntity.ok(reviewService.getReviewImg(reviewIdx));
    }

    @GetMapping("/modify/{reviewIdx}")
    public ResponseEntity<?> getModify(@PathVariable int reviewIdx) {
        try {
            Review review = reviewService.getModify(reviewIdx);
            if (review != null) {
                return new ResponseEntity<Review>(review, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return exceptionHandling(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> modify(
            @RequestBody ReviewModifyResponse reviewModifyResponse, HttpSession session) {
        try {
            reviewService.modify(reviewModifyResponse);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(int reviewIdx) {
        try {
            reviewService.delete(reviewIdx);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping("/hotplace")
    public ResponseEntity<?> mostReviews() throws Exception {
        return ResponseEntity.ok(reviewService.mostReviews());
    }

    @GetMapping("/score/{contentId}")
    public ResponseEntity<?> countScores(@PathVariable int contentId) throws Exception {
        return ResponseEntity.ok(reviewService.countScores(contentId));
    }

    /*
     * contentId가 주어지면, 그에 따른 리뷰 싹 다 가져오기
     */
    @GetMapping("/content/{contentId}")
    public ResponseEntity<?> getReviewsByContentId(@PathVariable int contentId) throws Exception {
        List<Review> list = reviewService.getReviewsByContentId(contentId);
        return ResponseEntity.ok(list);
    }

    /*
     * userId가 주어지면, 그에 따른 리뷰 싹 다 가져오기
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getReviewsByUserId(@PathVariable String userId) throws Exception {
        List<Review> list = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(list);
    }

    private ResponseEntity<?> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<String>(
                "Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
