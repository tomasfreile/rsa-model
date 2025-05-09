package si.rsamodel;

import si.rsamodel.encryptor.ChineseRemainderDecryptStrategy;
import si.rsamodel.encryptor.RSA;
import si.rsamodel.encryptor.StandardDecryptStrategy;
import si.rsamodel.key.RSAKeyGenerator;
import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSAKeyGenerator keyGen = new RSAKeyGenerator(2048);
        RsaKey key = (RsaKey) keyGen.generateKey();

        String message = "This is a test message for RSA encryption";
        BigInteger encrypted;

        // Encriptar una vez
        RSA rsaWithoutCRT = new RSA(new StandardDecryptStrategy());
        encrypted = rsaWithoutCRT.encrypt(message, key);

        // Desencriptar con CRT
        RSA rsaWithCRT = new RSA(new ChineseRemainderDecryptStrategy());
        long startWithCRT = System.nanoTime();
        String decryptedWithCRT = rsaWithCRT.decrypt(encrypted, key);
        long durationWithCRT = System.nanoTime() - startWithCRT;

        // Desencriptar sin CRT
        long startWithoutCRT = System.nanoTime();
        String decryptedWithoutCRT = rsaWithoutCRT.decrypt(encrypted, key);
        long durationWithoutCRT = System.nanoTime() - startWithoutCRT;

        System.out.println("Original: " + message);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted (with CRT): " + decryptedWithCRT);
        System.out.println("Decrypted (without CRT): " + decryptedWithoutCRT);
        System.out.println("Time with CRT: " + durationWithCRT / 1_000_000 + " ms");
        System.out.println("Time without CRT: " + durationWithoutCRT / 1_000_000 + " ms");
    }
}