package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReactionRepository extends JpaRepository<Reaction, Long> {

}