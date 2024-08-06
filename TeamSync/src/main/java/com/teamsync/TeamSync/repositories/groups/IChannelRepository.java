package com.teamsync.TeamSync.repositories.groups;
import com.teamsync.TeamSync.models.groups.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChannelRepository extends JpaRepository<Channel, Long> {

}