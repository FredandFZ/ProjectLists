package org.example.codesignconnect.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.model.ResultMessage;

@Slf4j
public class MessageUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String getMessage(boolean isSystemMessage,String fromName, Object message) throws Exception {
        try {
            ResultMessage result = new ResultMessage();
            result.setSystem(isSystemMessage);
            result.setMessage(message);
            if(fromName != null) {
                result.setFromName(fromName);
            }
            return objectMapper.writeValueAsString(result);
        }
        catch (Exception e) {
            throw new Exception("Serialization failed", e);
        }
    }
}
