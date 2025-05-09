package si.rsamodel.encryptor;

import si.rsamodel.math.BigIntegerUtil;
import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class RSA implements MessageEncryptor<RsaKey> {

    private final RsaDecryptStrategy decryptStrategy;

    public RSA(RsaDecryptStrategy decryptStrategy) {
        this.decryptStrategy = decryptStrategy;
    }

    @Override
    public BigInteger encrypt(String message, RsaKey key) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }

        BigInteger n = key.getPublicKey().getModulus();
        BigInteger e = key.getPublicKey().getExponent();
        BigInteger numericMessage = BigIntegerUtil.stringToBigInteger(message);
        BigInteger messageMod = numericMessage.mod(n);
        return messageMod.modPow(e, n);
    }

    @Override
    public String decrypt(BigInteger cipherText, RsaKey key) {
        BigInteger plainText = decryptStrategy.decrypt(cipherText, key);
        return BigIntegerUtil.bigIntegerToString(plainText);
    }
}