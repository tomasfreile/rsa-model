package si.rsamodel.encryptor;

import si.rsamodel.math.BigIntegerUtil;
import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class RSA implements MessageEncryptor<RsaKey> {

    private final boolean useChineseRemainder;

    public RSA() {
        this(true); // por defecto usa CRT
    }

    public RSA(boolean useChineseRemainder) {
        this.useChineseRemainder = useChineseRemainder;
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
        BigInteger d = key.getPrivateKey().getInverse();
        BigInteger n = key.getPublicKey().getModulus();

        if (!useChineseRemainder) {
            // Metodo clásico: c^d mod n
            return BigIntegerUtil.bigIntegerToString(cipherText.modPow(d, n));
        }

        // Metodo CRT: más eficiente
        BigInteger p = key.getPrivateKey().getP();
        BigInteger q = key.getPrivateKey().getQ();

        BigInteger m1 = cipherText.modPow(d, p);
        BigInteger m2 = cipherText.modPow(d, q);

        BigInteger qInv = q.modInverse(p);
        BigInteger h = (m1.subtract(m2)).multiply(qInv).mod(p);
        BigInteger m = m2.add(h.multiply(q));

        return BigIntegerUtil.bigIntegerToString(m);
    }
}