/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.statementProcessor.Controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Thayal
 */
@RestController
@RequestMapping("/controller")
public class DataProcessor {
    private static DataProcessor INSTANCE;

    private DataProcessor() {
    }
    
    
    @RequestMapping(value="/processInput",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String processInput(@RequestBody String input,HttpServletRequest request)
    {
        try{
            System.out.println(input);
        }
        catch(Exception e)
        {
            
        }
        return "Sucess";
    }
}
