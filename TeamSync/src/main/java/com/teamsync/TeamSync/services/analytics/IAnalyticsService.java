package com.teamsync.TeamSync.services.analytics;

import com.teamsync.TeamSync.dtos.analytics.ActiveUserDTO;
import com.teamsync.TeamSync.dtos.analytics.GroupPostsDTO;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.users.User;

import java.util.Collection;
import java.util.List;

public interface IAnalyticsService {
    Collection<Post> getMostPopularPosts(String period);
    Collection<ActiveUserDTO> getMostActiveUsers(Long groupId, String period);
    Collection<GroupPostsDTO> getGroupsPostsCount(String period);
}
