package com.example.mat.financialmanager.model;

import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class MutualFund extends Fund {

    public MutualFund() {
    }

    public MutualFund(String id, String userId, String name, double value, String bank, Date dateDue, String currency, String fundType) {
        super(id, userId, name, value, bank, dateDue, currency, fundType);
    }
}
