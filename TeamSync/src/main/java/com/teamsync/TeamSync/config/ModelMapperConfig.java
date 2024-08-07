package com.teamsync.TeamSync.config;

import com.teamsync.TeamSync.dtos.users.CreateUserDTO;
import com.teamsync.TeamSync.models.users.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

//    @Bean
//    public ModelMapper modelMapper(){
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.addMappings(new PropertyMap<CreateUserDTO, User>() {
//            @Override
//            protected void configure() {
//                map().setExternalId(source.getExternalId());
//            }
//        });
//
//        return modelMapper;
//    }
}