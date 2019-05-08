/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.utils;

import java.math.BigDecimal;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sendh
 */
public class RaboUtility {

    public String validate(JSONObject transaction, Map<String, String[]> validTransactions) throws JSONException {

        String jsonValidate = null;
        if (transaction.has("endBalance") && transaction.has("mutation") && transaction.has("startBalance")
                && transaction.has("description") && transaction.has("accountNumber") && transaction.has("reference")) {
            BigDecimal startBal = new BigDecimal(transaction.getString("startBalance")).setScale(2, BigDecimal.ROUND_CEILING);
            BigDecimal mutation = new BigDecimal(transaction.getString("mutation")).setScale(2, BigDecimal.ROUND_CEILING);
            BigDecimal endBalance = new BigDecimal(transaction.getString("endBalance")).setScale(2, BigDecimal.ROUND_CEILING);
            BigDecimal calc = mutation.add(startBal).setScale(2, BigDecimal.ROUND_CEILING);
            if (!endBalance.equals(calc)) {
                jsonValidate = "balNotMatching";

            } else if ((!validTransactions.isEmpty()
                    && validTransactions.containsKey(transaction.getString("reference")))) {
                jsonValidate = "duplicateTransaction";
            }
        }
        return jsonValidate;
    }

    public StringBuilder formResponse(Map<String, String[]> failedTransactions) {
        if (failedTransactions.isEmpty()) {
            return new StringBuilder("ALl transactions submitted for processing are valid");
        }
        StringBuilder response = new StringBuilder("<table border=\"1\" cellpadding=\"10\"><caption>Invalid Transactions</caption><tr><th>Transacton Reference</th><th>Description</th><th>Failure Reason</th></tr>");
        for (Map.Entry<String, String[]> entry : failedTransactions.entrySet()) {
            response.append("<tr><td>").append(entry.getValue()[0]).append("</td><td>").append(entry.getValue()[1]).append("</td><td>").append(entry.getValue()[2]).append("</td></tr>");
        }
        response.append("</table>");
        return response;
    }

    public JSONObject createJson(String[] values) throws JSONException {
        JSONObject transaction = new JSONObject();
        for (int i = 0; i < values.length; i++) {
            switch (i) {
                case 0:
                    if (!StringUtils.isBlank(values[i])) {
                        transaction.put("reference", values[i]);
                    }
                    break;
                case 1:
                    if (!StringUtils.isBlank(values[i])) {
                        transaction.put("accountNumber", values[i]);
                    }
                    break;
                case 2:
                    if (!StringUtils.isBlank(values[i])) {
                        transaction.put("description", values[i]);
                    }
                    break;
                case 3:
                    if (!StringUtils.isBlank(values[i])) {
                        transaction.put("startBalance", values[i]);
                    }
                    break;
                case 4:
                    if (!StringUtils.isBlank(values[i])) {
                        transaction.put("mutation", values[i]);
                    }
                    break;
                case 5:
                    if (!StringUtils.isBlank(values[i])) {
                        transaction.put("endBalance", values[i]);
                    }
                    break;
            }
        }
        return transaction;
    }
}
