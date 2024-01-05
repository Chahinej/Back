package com.example.SystemAlerte.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class GetPropertiesBean {

    private final String secret;
    private final Long access;
    private final Long refresh;

    @Autowired
    public GetPropertiesBean(@Value("${jwt.secret}") String secret, @Value("${jwt.access}") Long access,
            @Value("${jwt.refresh}") Long refresh) {
        this.secret = secret;
        this.access = access;
        this.refresh = refresh;

    }

}