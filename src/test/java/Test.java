import org.junit.jupiter.api.Test;
import si.rsamodel.encryptor.ChineseRemainderDecryptStrategy;
import si.rsamodel.encryptor.RSA;
import si.rsamodel.key.RSAKeyGenerator;
import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class RsaEncryptorTest {

    private final RSA rsa = new RSA(new ChineseRemainderDecryptStrategy());
    private final RsaKey key = (RsaKey) new RSAKeyGenerator(1024).generateKey();

    @Test
    void encryptAndDecryptReturnsOriginalMessage() {
        String originalMessage = "Hola";
        BigInteger encrypted = rsa.encrypt(originalMessage, key);
        String decrypted = rsa.decrypt(encrypted, key);

        assertEquals(originalMessage, decrypted);
    }

    @Test
    void encryptingSameMessageReturnsSameCiphertext() {
        String message = "Kotlin";
        BigInteger encrypted1 = rsa.encrypt(message, key);
        BigInteger encrypted2 = rsa.encrypt(message, key);

        assertEquals(encrypted1, encrypted2);
    }

    @Test
    void differentMessagesProduceDifferentCiphertexts() {
        String message1 = "Hola";
        String message2 = "Chau";

        BigInteger encrypted1 = rsa.encrypt(message1, key);
        BigInteger encrypted2 = rsa.encrypt(message2, key);

        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void emptyStringThrowsException() {
        String message = "";

        assertThrows(IllegalArgumentException.class, () -> {
            rsa.encrypt(message, key);
        });
    }

    @Test
    void nonAsciiCharactersEncryptAndDecrypt() {
        String message = "こんにちは"; // Japanese for "Hello"
        BigInteger encrypted = rsa.encrypt(message, key);
        String decrypted = rsa.decrypt(encrypted, key);

        assertEquals(message, decrypted);
    }
}