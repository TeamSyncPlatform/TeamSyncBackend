package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.services.posts.interfaces.IAttachmentService;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import com.teamsync.TeamSync.services.posts.interfaces.IUnreadPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/unread-posts")
public class UnreadPostController {
    private final IUnreadPostService unreadPostService;

    @GetMapping("/count")
    public ResponseEntity<Long> getUnreadPostsCount(@RequestParam Long userId, @RequestParam Long channelId) {
        Long unreadPostsCount = unreadPostService.getUnreadPostsCount(userId, channelId);
        return ResponseEntity.ok(unreadPostsCount);
    }

    @PutMapping("/update-last-read")
    public ResponseEntity<Void> updateLastReadTimestamp(@RequestParam Long userId, @RequestParam Long channelId) {
        unreadPostService.updateLastReadTimestamp(userId, channelId);
        return ResponseEntity.noContent().build();
    }

}