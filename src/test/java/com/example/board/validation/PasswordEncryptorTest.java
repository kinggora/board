package com.example.board.validation;

import com.example.board.util.PasswordEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class PasswordEncryptorTest {

    PasswordEncryptor encryptor = new PasswordEncryptor();

    @Test
    void encrypt() {
        String pwd = "12345";
        Map<String, String> map = encryptor.encrypt(pwd);
        String salt = map.get("salt");
        String hash = map.get("hash");

        Assertions.assertEquals(hash, encryptor.check(pwd, salt, hash));
    }
}