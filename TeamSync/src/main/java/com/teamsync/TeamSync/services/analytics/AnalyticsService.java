package com.teamsync.TeamSync.services.analytics;

import com.teamsync.TeamSync.dtos.analytics.ActiveUserDTO;
import com.teamsync.TeamSync.dtos.analytics.GroupPostsDTO;
import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService implements IAnalyticsService {

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Collection<ActiveUserDTO> getMostActiveUsers(Long groupId, String period) {
        Group group = groupRepository.findByIdAndIsDeletedFalse(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Date startDate = Date.from(calculateStartDate(period).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = new Date();

        List<Post> posts = postRepository.findAllByChannelGroupIdAndCreationDateBetween(
                groupId,
                startDate,
                endDate
        );

        Map<User, Long> userActivityCount = new HashMap<>();

        for (Post post : posts) {
            User author = post.getAuthor();
            userActivityCount.put(author, userActivityCount.getOrDefault(author, 0L) + 1);

            for (Comment comment : post.getComments()) {
                User commenter = comment.getAuthor();
                userActivityCount.put(commenter, userActivityCount.getOrDefault(commenter, 0L) + 1);
            }

            for (Map.Entry<Long, ReactionType> entry : post.getReactions().entrySet()) {
                Long userId = entry.getKey();
                Optional<User> reactor = userRepository.findByIdAndIsDeletedIsFalse(userId);
                if (reactor.isPresent()) {
                    User user = reactor.get();
                    userActivityCount.put(user, userActivityCount.getOrDefault(user, 0L) + 1);
                }
            }
        }

        return userActivityCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(entry -> new ActiveUserDTO(mapper.map(entry.getKey(), UserDTO.class), entry.getValue()))
                .collect(Collectors.toList());
    }


    private LocalDate calculateStartDate(String period) {
        LocalDate now = LocalDate.now();
        switch (period.toLowerCase()) {
            case "today":
                return now;
            case "this week":
                return now.with(java.time.DayOfWeek.MONDAY);
            case "this month":
                return now.withDayOfMonth(1);
            case "this year":
                return now.withDayOfYear(1);
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }

    @Override
    public Collection<Post> getMostPopularPosts(String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now().plusDays(1);

        List<Post> posts = postRepository.findByCreationDateBetweenAndChannelGroupIsDeletedFalse(
                Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );

        posts.sort((p1, p2) -> {
            int popularity1 = p1.getReactions().size() + p1.getComments().size();
            int popularity2 = p2.getReactions().size() + p2.getComments().size();
            return Integer.compare(popularity2, popularity1);
        });

        return posts.size() > 10 ? posts.subList(0, 10) : posts;
    }


    @Override
    public Collection<GroupPostsDTO> getGroupsPostsCount(String period) {
        Date startDate = Date.from(calculateStartDate(period).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = new Date();

        List<Group> groups = groupRepository.findAllByIsDeletedFalse();

        return groups.stream()
                .map(group -> {
                    Long postsCount = postRepository.countByChannelGroupIdAndCreationDateBetween(
                            group.getId(),
                            startDate,
                            endDate
                    );

                    GroupDTO groupDTO = mapper.map(group, GroupDTO.class);

                    return new GroupPostsDTO(groupDTO, postsCount);
                })
                .collect(Collectors.toList());
    }
}
