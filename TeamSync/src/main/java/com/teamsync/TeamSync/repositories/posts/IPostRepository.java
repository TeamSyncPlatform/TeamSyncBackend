package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

}