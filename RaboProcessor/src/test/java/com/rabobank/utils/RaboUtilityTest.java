/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.utils;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.json.JSONObject;

/**
 *
 * @author sendh
 */
public class RaboUtilityTest extends TestCase {

    public RaboUtilityTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of validate method, of class RaboUtility.
     */
    public void testValidate() throws Exception {
        System.out.println("Validate Duplicate transactions");
        JSONObject transaction = new JSONObject();
        transaction.put("reference", "194261");
        transaction.put("accountNumber", "NL91RABO0315273637");
        transaction.put("description", "Clothes from Jan Bakker");
        transaction.put("startBalance", "21.6");
        transaction.put("mutation", "-41.83");
        transaction.put("endBalance", "-20.23");

        Map<String, String[]> validTransactions = new HashMap<>();
        validTransactions.put("194261", new String[]{"194261", "Clothes from Jan Bakker"});
        validTransactions.put("1231231", new String[]{"1231231", "test data"});
        validTransactions.put("183049", new String[]{"183049", "Clothes for Willem Dekker"});
        RaboUtility instance = new RaboUtility();
        String expResult = "duplicateTransaction";
        String result = instance.validate(transaction, validTransactions);
        assertEquals(expResult, result);

        System.out.println("Validate distinct transactions");
        JSONObject transactionDistinct = new JSONObject();
        transactionDistinct.put("reference", "299999");
        transactionDistinct.put("accountNumber", "N1291RABO0315273637");
        transactionDistinct.put("description", "Clothes from Jan Bakker");
        transactionDistinct.put("startBalance", "21.6");
        transactionDistinct.put("mutation", "-41.83");
        transactionDistinct.put("endBalance", "-20.23");

        expResult = null;
        result = instance.validate(transactionDistinct, validTransactions);
        assertEquals(expResult, result);

        System.out.println("Validate Balance Calculation - Correct");
        transactionDistinct.put("reference", "199261");
        transactionDistinct.put("accountNumber", "NL91RABO0315273637");
        transactionDistinct.put("description", "Clothes from Jan Bakker");
        transactionDistinct.put("startBalance", "21.6");
        transactionDistinct.put("mutation", "-41.83");
        transactionDistinct.put("endBalance", "-20.23");

        expResult = null;
        result = instance.validate(transactionDistinct, validTransactions);
        assertEquals(expResult, result);

        System.out.println("Validate Balance Calculation - InCorrect");
        transactionDistinct.put("reference", "199261");
        transactionDistinct.put("accountNumber", "NL91RABO0315273637");
        transactionDistinct.put("description", "Clothes from Jan Bakker");
        transactionDistinct.put("startBalance", "21.6");
        transactionDistinct.put("mutation", "-41.83");
        transactionDistinct.put("endBalance", "-10.23");

        expResult = "balNotMatching";
        result = instance.validate(transactionDistinct, validTransactions);
        assertEquals(expResult, result);

        System.out.println("Validate Balance Calculation - InCorrect");
        transactionDistinct.put("reference", "194261");
        transactionDistinct.put("accountNumber", "NL91RABO0315273637");
        transactionDistinct.put("description", "Clothes from Jan Bakker");
        transactionDistinct.put("startBalance", "21.6");
        transactionDistinct.put("mutation", "-41.83");
        transactionDistinct.put("endBalance", "-10.23");

        expResult = "balNotMatching";
        result = instance.validate(transactionDistinct, validTransactions);
        assertEquals(expResult, result);
    }

    /**
     * Test of formResponse method, of class RaboUtility.
     */
    public void testFormResponse() {
        System.out.println("formResponse");
        Map failedTransactions = new HashMap();
        failedTransactions.put("194261", new String[]{"194261", "Clothes from Jan Bakker", "duplicateTransaction"});
        failedTransactions.put("1231231", new String[]{"1231231", "test data", "balNotMatching"});
        failedTransactions.put("183049", new String[]{"183049", "Clothes for Willem Dekker", "duplicateTransaction"});
        RaboUtility instance = new RaboUtility();
        StringBuilder expResult = new StringBuilder("<table border=\"1\" cellpadding=\"10\"><caption>Invalid Transactions</caption><tr><th>Transacton Reference</th><th>Description</th><th>Failure Reason</th></tr>");
        expResult.append("<tr><td>183049</td><td>Clothes for Willem Dekker</td><td>duplicateTransaction</td></tr>");
        expResult.append("<tr><td>194261</td><td>Clothes from Jan Bakker</td><td>duplicateTransaction</td></tr>");
        expResult.append("<tr><td>1231231</td><td>test data</td><td>balNotMatching</td></tr>");
        expResult.append("</table>");
        StringBuilder result = instance.formResponse(failedTransactions);
        assertEquals(expResult.toString(), result.toString());
    }
    /**
     * Test of createJson method, of class RaboUtility.
     */
    public void testCreateJson() throws Exception {
        System.out.println("createJson");
        String[] values =  new String[]{"112806", "NL27SNSB0917829871", "Clothes for Willem Dekker","91.23","15.57","106.8"};
        RaboUtility instance = new RaboUtility();
        JSONObject expResult = new JSONObject();
        expResult.put("reference", "112806");
        expResult.put("accountNumber", "NL27SNSB0917829871");
        expResult.put("description", "Clothes for Willem Dekker");
        expResult.put("startBalance", "91.23");
        expResult.put("mutation", "15.57");
        expResult.put("endBalance", "106.8");
        
        JSONObject result = instance.createJson(values);
        assertEquals(expResult.toString(), result.toString());
    }
}
