/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.uploadfilestoawss3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */
public class Main {

    public static void main(String... args) {
        try {
            Main.uploadFiles("snpi-received");
        } catch (IOException | AmazonClientException | InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void uploadFiles(String bucket) throws IOException, AmazonClientException, AmazonServiceException, InterruptedException {
        TransferManager transfer = TransferManagerBuilder.standard().build();
        List<File> files = new ArrayList<>();
        Files.list(Paths.get("C:\\Users\\lisbet\\Desktop\\Mensajes"))
                .forEach(path -> {
                    files.add(new File(path.toUri()));
                });
        System.out.println("Initial time: " + new Date().toInstant().toString());
        MultipleFileUpload multipleUpload = transfer.uploadFileList(bucket, null, new File("C:\\Users\\lisbet\\Desktop\\Mensajes"), files);
        while (!multipleUpload.isDone()) {
            System.out.println("Percent: " + multipleUpload.getProgress().getPercentTransferred());
        }
        multipleUpload.waitForCompletion();
        System.out.println("Process complete!!!!");
        transfer.shutdownNow();
        System.out.println("Ending time: " + new Date().toInstant().toString());
        files.forEach(file -> {
            try {
                Files.delete(Paths.get(file.toURI()));
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
