package mindbadger.api.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class ResponseEntityCreator {
    public ResponseEntity<Object> createFor(String operationName, Map<String, String> requestParameters) {
        HttpStatus httpStatus = null;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        return new ResponseEntity<Object>(headers, httpStatus);
    }
}
