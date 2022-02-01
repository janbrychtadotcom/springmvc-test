package com.test.share;

import org.osgi.service.component.annotations.Component;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.UUID;

@Component
public class SharedAppBean {

    public SharedAppBean() {}

    private final String id = UUID.randomUUID().toString();


    public String getId() {
        return id;
    }


}
