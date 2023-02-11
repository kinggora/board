package com.example.board.model;

public class AttachFile {

    public AttachFile(String origName, String storeName, String ext, String storePath) {
        this.origName = origName;
        this.storeName = storeName;
        this.ext = ext;
        this.storeDir = storePath;
    }

    private String origName;
    private String storeName;
    private String ext;

    private String storeDir;

    public String getOrigName() {
        return origName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getExt() {
        return ext;
    }

    public String getStoreDir() {
        return storeDir;
    }
}
