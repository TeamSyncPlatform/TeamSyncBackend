package com.teamsync.TeamSync.controllers.analytics;

import com.teamsync.TeamSync.dtos.analytics.ActiveUserDTO;
import com.teamsync.TeamSync.dtos.posts.post.PostDTO;
import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.services.analytics.IAnalyticsService;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/analytics")
public class AnalyticsController {
    private final IAnalyticsService service;
    private final ModelMapper mapper;
    @GetMapping("most-popular-posts/{period}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<PostDTO>> getMostPopularPosts(@PathVariable String period) {
        Collection<Post> posts = service.getMostPopularPosts(period);
        Collection<PostDTO> postResponses = posts.stream()
                .map(post -> mapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    @GetMapping("most-active-users/{groupId}/{period}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<ActiveUserDTO>> getMostActiveUsers(@PathVariable Long groupId, @PathVariable String period) {
        Collection<ActiveUserDTO> users = service.getMostActiveUsers(groupId, period);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
