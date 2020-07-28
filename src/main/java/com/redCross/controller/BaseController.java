package com.redCross.controller;

import com.alipay.api.domain.Account;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @Value("${RedCross.upload.file.path}")
    String rootPath;

    public static final String Integer_MAX_VALUE = "" + Integer.MAX_VALUE;

    //protected

    public String getCurrentUsername() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }


}
