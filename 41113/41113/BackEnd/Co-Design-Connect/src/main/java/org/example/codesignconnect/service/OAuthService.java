package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Token;

public interface OAuthService {
    public Token refreshMiroToken();
    public Token refreshGoogleDocsToken();
}
