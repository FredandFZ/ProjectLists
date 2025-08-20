package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
    @Autowired
    private OAuthService oAuthService;

    @GetMapping("/miro/tokens")
    public Result getMiroToken(){
        return Result.success(oAuthService.refreshMiroToken());
    }

    @GetMapping("/google/tokens")
    public Result getGoogleDocsToken(){
        return Result.success(oAuthService.refreshGoogleDocsToken());
    }
}
