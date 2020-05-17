package com.gtw.drools.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author gtw
 */
@Data
public class UserInfoFact {
    private String name;
    private int age;
    private List<String> interests;
    private Map<String, String> tags;
}
