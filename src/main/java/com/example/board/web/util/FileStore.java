package com.example.board.web.util;

import com.example.board.web.model.AttachFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URL;
import java.util.*;

public class FileStore {

    public static List<AttachFile> uploadFiles(int id, List<Part> parts) throws IOException {
        List<AttachFile> files = new ArrayList<>();
        for(Part part : parts){
            String origFileName = part.getSubmittedFileName();
            String extension = extracted(origFileName);
            String storeFileName = createStoreFileName(extension);
            String storeDir = getStoragePath() + File.separator;

            part.write(storeDir + storeFileName);

            AttachFile attachFile = AttachFile.builder()
                    .postId(id)
                    .origName(origFileName)
                    .storeName(storeFileName)
                    .ext(extension)
                    .storeDir(storeDir)
                    .build();
            files.add(attachFile);
        }
        return files;
    }

    public static void downloadFile(AttachFile attachFile, HttpServletResponse response){
        File file = new File(attachFile.getStoreDir(), attachFile.getStoreName());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String origName = attachFile.getOrigName();
        try {
            origName = new String(origName.getBytes("utf-8"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Content-disposition", "attachment; fileName=" + origName);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024 * 8];
            while(true){
                int count = fis.read(buffer);
                if(count == -1){
                    break;
                }
                os.write(buffer, 0, count);
            }
            os.flush();

            os.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
