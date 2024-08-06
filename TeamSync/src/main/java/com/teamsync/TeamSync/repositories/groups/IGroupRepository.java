package com.teamsync.TeamSync.repositories.groups;

import com.teamsync.TeamSync.models.groups.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IGroupRepository extends JpaRepository<Group, Long> {

}