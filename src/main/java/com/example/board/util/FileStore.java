package com.example.board.util;

import com.example.board.model.AttachFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FileStore {

    public static List<AttachFile> storeFiles(List<Part> parts) throws IOException {
        List<AttachFile> files = new ArrayList<>();
        for(Part part : parts){
            String origFileName = part.getSubmittedFileName();
            String extension = extracted(origFileName);
            String storeFileName = createStoreFileName(extension);
            String storeDir = getStoragePath() + File.separator;

            part.write(storeDir + storeFileName);
            AttachFile attachFile = AttachFile.builder()
                    .origName(origFileName)
                    .storeName(storeFileName)
                    .ext(extension)
                    .storeDir(storeDir)
                    .build();
            files.add(attachFile);
        }
        return files;
    }

    private static String getStoragePath() throws IOException {
        URL resource = FileStore.class.getClassLoader().getResource("application.properties");
        Properties properties = new Properties();
        properties.load(resource.openStream());
        return properties.getProperty("file.storage");
    }

    public static void deleteFiles(List<AttachFile> attachFiles){
        for(AttachFile attachFile : attachFiles){
            File file = new File(attachFile.getStoreDir() + attachFile.getStoreName());
            if(file.exists()){
                file.delete();
            }
        }
    }

    private static String createStoreFileName(String ext){
        return UUID.randomUUID() + "." + ext;
    }

    private static String extracted(String OrigFileName){
        int pos = OrigFileName.lastIndexOf(".");
        return OrigFileName.substring(pos+1);
    }

}
