package com.example.mat.financialmanager.model;

import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class MutualFund extends Fund {

    public MutualFund() {
    }

    public MutualFund(String id, String name, double value, String bank, Date dateDue) {
        super(id, name, value, bank, dateDue);
    }
}
