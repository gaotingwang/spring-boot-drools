package com.gtw.drools.model;

import java.io.Serializable;

public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;
    
        
    private Double percentage;

    
    public Discount() {
    }

    public Discount(Double percentage) {
        this.percentage = percentage;
    }
        

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    
    
}
