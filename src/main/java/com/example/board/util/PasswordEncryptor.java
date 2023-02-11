package com.example.board.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class PasswordEncryptor {
    private static final int SALT_SIZE = 16;

    public Map<String, String> encrypt(String pwd){
        String salt = createSalt();
        String hash = getHashCode(salt, pwd);

        Map<String, String> map = new HashMap<>();
        map.put("salt", salt);
        map.put("hash", hash);
        return map;
    }

    public boolean check(String pwd, String salt, String hash){
        if(getHashCode(salt, pwd).equals(hash)){
            return true;
        } else {
            return false;
        }
    }

    private String getHashCode(String salt, String pwd){
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((pwd+salt).getBytes());
            byte[] bytes = md.digest();

            StringBuffer sb = new StringBuffer();
            for(byte b : bytes){
                sb.append(String.format("%02x", b));
            }

            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private String createSalt(){
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        r.nextBytes(salt);

        //16진수 변환
        StringBuffer sb = new StringBuffer();
        for(byte b : salt){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


}
