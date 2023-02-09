package com.example.board.util;

import com.example.board.dto.AttachFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileStore {

    private static String FILE_DIR = "C:/Users/HYUNA/IdeaProjects/board/build/libs/exploded/board-1.0-SNAPSHOT.war/upload";

    public List<AttachFile> storeFiles(List<Part> parts) throws IOException {
        List<AttachFile> files = new ArrayList<>();
        for(Part part : parts){
            InputStream is = part.getInputStream();
            while(is.read() != -1){

            }
            String origFileName = part.getSubmittedFileName();
            String extension = extracted(origFileName);
            String storeFileName = createStoreFileName(extension);
            String storeDir = FILE_DIR + File.separator;
            part.write(storeDir+storeFileName);

            files.add(new AttachFile(origFileName, storeFileName, extension, storeDir));
        }
        return files;
    }

    private String createStoreFileName(String ext){
        return UUID.randomUUID() + "." + ext;
    }

    private String extracted(String OrigFileName){
        int pos = OrigFileName.lastIndexOf(".");
        return OrigFileName.substring(pos+1);
    }

}
