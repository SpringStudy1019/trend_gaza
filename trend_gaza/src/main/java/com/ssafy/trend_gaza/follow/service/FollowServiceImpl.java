package com.ssafy.trend_gaza.follow.service;


import com.ssafy.trend_gaza.follow.dto.FollowRequest;
import com.ssafy.trend_gaza.follow.entity.Follow;
import com.ssafy.trend_gaza.follow.repository.FollowMapper;
import com.ssafy.trend_gaza.user.entity.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

    private FollowMapper followMapper;

    public FollowServiceImpl(FollowMapper followMapper) {
        super();
        this.followMapper = followMapper;
    }

    @Override
    public int onFollow(FollowRequest followRequest) throws Exception {
        int result = 0;
        Follow follow = followMapper.findFollow(followRequest);
        System.out.println(follow);
        if (follow == null) {
            result = followMapper.onFollow(followRequest);
        }
        return result;
    }

    @Override
    public int offFollow(Map<String, String> param) throws Exception {
        int result = 0;
        FollowRequest followRequest = new FollowRequest();
        followRequest.setFolloweeId(param.get("followeeId"));
        followRequest.setFollowerId(param.get("followerId"));
        Follow follow = followMapper.findFollow(followRequest);
        if (follow != null) {
            result = followMapper.offFollow(param);
        }
        return result;
    }

    @Override
    public List<User> listFollowers(String followeeId) throws Exception {
        return followMapper.listFollowers(followeeId);
    }

    @Override
    public int followCount(String followerId) throws Exception {
        return followMapper.followCount(followerId);
    }

    @Override
    public Follow findFollow(FollowRequest followRequest) throws SQLException {
        return followMapper.findFollow(followRequest);
    }

    @Override
    public List<User> listRelated(String currentUserId, String followeeId) throws Exception {
        return followMapper.listRelated(currentUserId, followeeId);
    }
}
