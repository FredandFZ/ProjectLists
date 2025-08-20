package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.codesignconnect.model.Token;

@Mapper
public interface TokenMapper {
    Token getTokenByType(String type);
    void insertToken(Token token);
    void deleteTokenByType(String type);
    void updateTokenByType(Token token);
}
