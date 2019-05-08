/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.raboprocessor;

import com.rabobank.utils.RaboUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.json.JSONException;
import org.json.XML;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author sendh
 */
@Controller
@RequestMapping(value = "/autoResolve")
public class RaboProcessor {

    @RequestMapping(value = "/processor", headers = ("content-type=multipart/*"),
            method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity process(@RequestParam("file") MultipartFile inputFile, HttpServletRequest request) throws IOException, JSONException {

        String[] extensionArray = inputFile.getOriginalFilename().split("\\.");
        String extension = extensionArray.length > 0 ? extensionArray[1] : null;

        if (inputFile.isEmpty() || !"csv".equalsIgnoreCase(extension)) {
            return new ResponseEntity("Invalid Input File.Please Upload Excel file with extension (.xlsx)", HttpStatus.OK);
        }

        Map<String, String[]> failedTransactions = new HashMap<>();
        Map<String, String[]> validTransactions = new HashMap<>();
        RaboUtility rabo = new RaboUtility();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputFile.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                JSONObject transaction = new JSONObject();
                String[] values = line.split(",");
                if (values[0].contains("Reference")) {
                    continue;
                }
                transaction = rabo.createJson(values);
                String validate = rabo.validate(transaction, validTransactions);
                if (null != validate && "balNotMatching".equalsIgnoreCase(validate)) {
                    failedTransactions.put(transaction.getString("reference"), new String[]{transaction.getString("reference"), transaction.getString("description"), "Balance MisCalcualted"});
                } else if (null != validate && "duplicateTransaction".equalsIgnoreCase(validate)) {
                    failedTransactions.put(transaction.getString("reference"), new String[]{transaction.getString("reference"), transaction.getString("description"), "Duplicate Transaction"});
                } else {
                    validTransactions.put(transaction.getString("reference"), new String[]{transaction.getString("reference"), transaction.getString("description")});
                }

            }
        }

        return new ResponseEntity(rabo.formResponse(failedTransactions).toString(), HttpStatus.OK);

    }

    @RequestMapping(value = "/xmlProcessor", headers = ("content-type=multipart/*"),
            method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity processXml(@RequestParam("file") MultipartFile inputFile, HttpServletRequest request) throws IOException, JSONException {

        String[] extensionArray = inputFile.getOriginalFilename().split("\\.");
        String extension = extensionArray.length > 0 ? extensionArray[1] : null;

        if (inputFile.isEmpty() || !"xml".equalsIgnoreCase(extension)) {
            return new ResponseEntity("Invalid Input File.Please Upload Excel file with extension (.xlsx)", HttpStatus.OK);
        }

        Map<String, String[]> failedTransactions = new HashMap<>();
        Map<String, String[]> validTransactions = new HashMap<>();
        String data = new String(inputFile.getBytes());
        JSONObject xmlJSONObj = XML.toJSONObject(data);
        JSONObject jsnobject = new JSONObject(xmlJSONObj);
        JSONArray jsonArray = null;
        RaboUtility rabo = new RaboUtility();
        System.out.println("" + jsnobject);
        if (xmlJSONObj.getJSONObject("records").get("record") instanceof JSONObject) {
            jsonArray = new JSONArray("[" + xmlJSONObj.getJSONObject("records").get("record").toString() + "]");
        } else {
            jsonArray = xmlJSONObj.getJSONObject("records").getJSONArray("record");
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject transaction = jsonArray.getJSONObject(i);
            String validate = rabo.validate(transaction, validTransactions);
            if (null != validate && "balNotMatching".equalsIgnoreCase(validate)) {
                    failedTransactions.put(transaction.getString("reference"), new String[]{transaction.getString("reference"), transaction.getString("description"), "Balance MisCalcualted"});
                } else if (null != validate && "duplicateTransaction".equalsIgnoreCase(validate)) {
                    failedTransactions.put(transaction.getString("reference"), new String[]{transaction.getString("reference"), transaction.getString("description"), "Duplicate Transaction"});
                } else {
                    validTransactions.put(transaction.getString("reference"), new String[]{transaction.getString("reference"), transaction.getString("description")});
                }
        }
        return new ResponseEntity(rabo.formResponse(failedTransactions).toString(), HttpStatus.OK);

    }
}
