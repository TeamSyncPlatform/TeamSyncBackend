package com.teamsync.TeamSync.repositories.users;

import com.teamsync.TeamSync.models.users.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
}
