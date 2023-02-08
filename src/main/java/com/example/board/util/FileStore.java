package com.example.board.util;

import com.example.board.dto.AttachFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileStore {

    private static String FILE_DIR = "C://Users/HYUNA/IdeaProjects/upload/";

    public List<AttachFile> storeFiles(List<Part> parts) throws IOException {
        List<AttachFile> files = new ArrayList<>();
        for(Part part : parts){
            String origFileName = part.getSubmittedFileName();
            String extension = extracted(origFileName);
            String storeFileName = createStoreFileName(extension);
            part.write(FILE_DIR + storeFileName);

            files.add(new AttachFile(origFileName, storeFileName, extension, FILE_DIR));
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
