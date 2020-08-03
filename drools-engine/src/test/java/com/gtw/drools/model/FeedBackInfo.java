package com.gtw.drools.model;

import lombok.Data;

@Data
public class FeedBackInfo {
    private String dataId;
    private String submitUser;
    private String errorDesc;
    private String userGroup;
    private String description;
    private String responseLevel;
    private String modifyPri;
    private String repeatModifyFlag;
    private int fetchPriority;
}
