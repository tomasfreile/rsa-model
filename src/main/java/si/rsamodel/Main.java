package si.rsamodel;

import si.rsamodel.encryptor.RSA;
import si.rsamodel.key.RSAKeyGenerator;
import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSAKeyGenerator keyGen = new RSAKeyGenerator(512);
        RsaKey key = (RsaKey) keyGen.generateKey();

        RSA rsa = new RSA();

        String message = "This is a test message for RSA encryption";
        BigInteger encrypted = rsa.encrypt(message, key);
        String decrypted = rsa.decrypt(encrypted, key);

        System.out.println("Original: " + message);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}