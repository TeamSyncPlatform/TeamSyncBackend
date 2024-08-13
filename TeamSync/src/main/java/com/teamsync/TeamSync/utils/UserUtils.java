package com.teamsync.TeamSync.utils;

import com.teamsync.TeamSync.models.users.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {
    public DefaultOAuth2AuthenticatedPrincipal getPrincipal(){
        return (DefaultOAuth2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User getLoggedUser(){
        DefaultOAuth2AuthenticatedPrincipal principal = getPrincipal();
        User user = new User();
        user.setEmail(principal.getAttributes().get("email").toString());
        user.setFirstName(principal.getAttributes().get("given_name").toString());
        user.setLastName(principal.getAttributes().get("family_name").toString());
        user.setExternalIdentification(principal.getName());
        user.setRole(getRole(principal));
        return user;
    }

    private String getRole(DefaultOAuth2AuthenticatedPrincipal principal){
        return principal.getAuthorities().toString().replace("]","").split("_")[1];
    }
}
