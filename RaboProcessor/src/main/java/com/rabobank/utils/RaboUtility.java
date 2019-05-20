/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.utils;

import com.rabobank.dto.RaboDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author sendh
 */
public class RaboUtility {

    private final Logger LOGGER = Logger.getLogger(RaboUtility.class.getName());

    // Business Validations - Finding Duplicate Transactions and finding transactions with incorrect Balance

    public RaboDto validate(JSONObject transaction, RaboDto raboDto) throws JSONException {

        if (transaction.has("endBalance") && transaction.has("mutation") && transaction.has("startBalance")
                && transaction.has("description") && transaction.has("accountNumber") && transaction.has("reference")) {
            BigDecimal startBal = new BigDecimal(transaction.get("startBalance").toString()).setScale(2, BigDecimal.ROUND_CEILING);
            BigDecimal mutation = new BigDecimal(transaction.get("mutation").toString()).setScale(2, BigDecimal.ROUND_CEILING);
            BigDecimal endBalance = new BigDecimal(transaction.get("endBalance").toString()).setScale(2, BigDecimal.ROUND_CEILING);
            BigDecimal calc = mutation.add(startBal).setScale(2, BigDecimal.ROUND_CEILING);
            if (!endBalance.equals(calc)) {
                raboDto.getFailedTransactions().put(transaction.get("reference").toString(), new String[]{transaction.get("reference").toString(), transaction.get("description").toString(), "Balance MisCalcualted"});

            } else if (null != raboDto.getValidTransactions() && !raboDto.getValidTransactions().isEmpty()
                    && raboDto.getValidTransactions().containsKey(transaction.get("reference").toString())) {
                raboDto.getFailedTransactions().put(transaction.get("reference").toString(), new String[]{transaction.get("reference").toString(), transaction.get("description").toString(), "Duplicate entry"});
            }
        }
        raboDto.getValidTransactions().put(transaction.get("reference").toString(), new String[]{transaction.get("reference").toString(), transaction.get("description").toString()});
        return raboDto;

    }

    // frame response from the DTO 
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

    // For Data loaded from CSV file - Frame JSON OBject
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

    // Parse the input File and load data into DTO
    public RaboDto loadData(MultipartFile inputFile, RaboDto raboDto, String fileType) throws IOException, JSONException {
        RaboUtility raboUtility = new RaboUtility();

        switch (fileType) {
            // Load Data from CSV file
            case "csv":
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputFile.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        // Skip the firt row in Excel - Headings
                        if (values[0].contains("Reference")) {
                            continue;
                        }
                        raboDto.getInputTransactionArr().put(raboUtility.createJson(values));
                    }
                } catch (IOException | JSONException ex) {
                    LOGGER.log(Level.SEVERE, "Exception occur", ex);

                }
                break;
            case "xml":
                // Load data from CSV
                String data = new String(inputFile.getBytes());
                JSONObject xmlJSONObj = XML.toJSONObject(data);
                if (xmlJSONObj.getJSONObject("records").get("record") instanceof JSONObject) {
                    raboDto.getInputTransactionArr().put(new JSONObject("[" + xmlJSONObj.getJSONObject("records").get("record").toString() + "]"));
                } else {
                    raboDto.setInputTransactionArr(xmlJSONObj.getJSONObject("records").getJSONArray("record"));
                }
                break;
        }
        return raboDto;
    }

    //Iterate Each Element in DTO , validate them 
    public RaboDto processData(RaboDto raboDto) {

        raboDto.getInputTransactionArr().forEach(item -> {
            JSONObject transaction = (JSONObject) item;
            validate(transaction, raboDto);
        });
        return raboDto;

    }
}
