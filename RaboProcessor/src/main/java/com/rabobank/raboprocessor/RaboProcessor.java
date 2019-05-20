/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.raboprocessor;

import com.rabobank.dto.RaboDto;
import com.rabobank.utils.RaboUtility;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.logging.Logger;

/**
 *
 * @author sendh
 */
@Controller
@RequestMapping(value = "/autoResolve")
public class RaboProcessor {

    private final Logger LOGGER = Logger.getLogger(RaboProcessor.class.getName());

    @RequestMapping(value = "/processor", headers = ("content-type=multipart/*"),
            method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity process(@RequestParam("file") MultipartFile inputFile, HttpServletRequest request) throws IOException, JSONException {
        RaboUtility util = new RaboUtility();
        RaboDto raboDto = new RaboDto();
        String fileType = null;
        String[] extensionArray = inputFile.getOriginalFilename().split("\\.");
        String extension = extensionArray.length > 0 ? extensionArray[1] : null;

        // Validate INput FIle Format
        if (inputFile.isEmpty() || "xml".equalsIgnoreCase(extension)) {
            fileType = "xml";
        } else if (inputFile.isEmpty() || "csv".equalsIgnoreCase(extension)) {
            fileType = "csv";
        }
        else if (inputFile.isEmpty() || !"csv".equalsIgnoreCase(extension) || !"xml".equalsIgnoreCase(extension)) {
            // If FIle format is not CSV or XML return back 
            LOGGER.info("Invalid Input File.Please Upload Excel file with extension (.xlsx) or xml file with extension (.xml)");
            return new ResponseEntity("Invalid Input File.Please Upload Excel file with extension (.xlsx)", HttpStatus.OK);
        }
        // Load data from input file to DTO
        util.loadData(inputFile, raboDto, fileType);
        // Validate the Data loaded in DTO
        util.processData(raboDto);
        return new ResponseEntity(util.formResponse(raboDto.getFailedTransactions()).toString(), HttpStatus.OK);

    }

}
