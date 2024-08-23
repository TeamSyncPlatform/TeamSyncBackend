package com.teamsync.TeamSync.services.posts.interfaces;

public interface IUnreadPostService {
    void updateLastReadTimestamp(Long userId, Long channelId);

    Long getUnreadPostsCount(Long userId, Long channelId);

    void initializeUnreadPostsForNewGroupMember(Long userId, Long groupId);

    void removeUnreadPostsForGroupMember(Long userId, Long groupId);

    void initializeUnreadPostsForNewChannel(Long channelId);
}
