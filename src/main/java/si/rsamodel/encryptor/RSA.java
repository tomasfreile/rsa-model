package si.rsamodel.encryptor;

import si.rsamodel.math.BigIntegerUtil;
import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class RSA implements MessageEncryptor<RsaKey> {

    @Override
    public BigInteger encrypt(String message, RsaKey key) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }

        BigInteger m = key.getPublicKey().getModulus();
        BigInteger e = key.getPublicKey().getExponent();
        BigInteger numericMessage = BigIntegerUtil.stringToBigInteger(message);
        BigInteger messageMod = numericMessage.mod(m);
        return messageMod.modPow(e, m);
    }

    @Override
    public String decrypt(BigInteger message, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();
        BigInteger m = key.getPublicKey().getModulus();
        BigInteger decrypted = message.modPow(d, m);
        return BigIntegerUtil.bigIntegerToString(decrypted);
    }
}