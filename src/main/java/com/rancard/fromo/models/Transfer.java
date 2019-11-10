package com.rancard.fromo.models;

import com.rancard.fromo.utils.ULID;

import java.util.HashMap;
import java.util.Map;

public class Transfer {
    private String id;

    private String currency;
    private String amount;
    private Long logDate;
    private String sender;
    private String receiver;

    public Transfer(){
        this.id = ULID.generate();
    }

    public Transfer(String currency, String amount, String sender, String receiver) {
        this.currency = currency;
        this.amount = amount;
        this.logDate = System.currentTimeMillis();
        this.sender = sender;
        this.receiver = receiver;
        this.id = ULID.generate();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getLogDate() {
        return logDate;
    }

    public void setLogDate(Long logDate) {
        this.logDate = logDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("currency",this.currency);
        map.put("amount",this.amount);
        map.put("sender",this.sender);
        map.put("receiver",this.receiver);
        return map;
    }
}