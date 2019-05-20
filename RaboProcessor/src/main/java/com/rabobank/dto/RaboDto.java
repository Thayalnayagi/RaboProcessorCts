/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.dto;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author sendh
 */
public class RaboDto {

    JSONArray inputTransactionArr = new JSONArray();
    Map<String, String[]> failedTransactions = new HashMap<>();
    Map<String, String[]> validTransactions = new HashMap<>();

    public JSONArray getInputTransactionArr() {
        return inputTransactionArr;
    }

    public void setInputTransactionArr(JSONArray inputTransactionArr) {
        this.inputTransactionArr = inputTransactionArr;
    }

    public Map<String, String[]> getFailedTransactions() {
        return failedTransactions;
    }

    public void setFailedTransactions(Map<String, String[]> failedTransactions) {
        this.failedTransactions = failedTransactions;
    }

    public Map<String, String[]> getValidTransactions() {
        return validTransactions;
    }

    public void setValidTransactions(Map<String, String[]> validTransactions) {
        this.validTransactions = validTransactions;
    }
    public RaboDto clear(RaboDto dto)
    {
        dto.getFailedTransactions().clear();
        dto.getValidTransactions().clear();
        return dto;
    }

}
