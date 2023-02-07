package com.example.board.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptorTest {

    PasswordEncryptor encryptor = new PasswordEncryptor();

    @Test
    void encrypt() {
        String pwd = "12345";
        Map<String, String> map = encryptor.encrypt(pwd);
        String salt = map.get("salt");
        String hash = map.get("hash");
        System.out.println("salt = " + salt);
        System.out.println("hash = " + hash);

        Assertions.assertEquals(hash, encryptor.check(pwd, salt, hash));
    }
}