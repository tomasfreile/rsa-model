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

        BigInteger n = key.getPublicKey().getModulus();
        BigInteger e = key.getPublicKey().getExponent();
        BigInteger numericMessage = BigIntegerUtil.stringToBigInteger(message);
        BigInteger messageMod = numericMessage.mod(n);
        return messageMod.modPow(e, n);
    }

    @Override
    public String decrypt(BigInteger cipherText, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();

        BigInteger p = key.getPrivateKey().getP();
        BigInteger q = key.getPrivateKey().getQ();

        BigInteger m1 = cipherText.modPow(d, p);

        BigInteger m2 = cipherText.modPow(d, q);

        BigInteger qInv = q.modInverse(p); // q⁻¹ mod p
        BigInteger h = (m1.subtract(m2)).multiply(qInv).mod(p);

        BigInteger m = m2.add(h.multiply(q)); // m ≡ m2 + h * q mod n

        return BigIntegerUtil.bigIntegerToString(m);
    }
}