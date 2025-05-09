package si.rsamodel.encryptor;

import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class ChineseRemainderDecryptStrategy implements RsaDecryptStrategy {

    @Override
    public BigInteger decrypt(BigInteger cipherText, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();
        BigInteger p = key.getPrivateKey().getP();
        BigInteger q = key.getPrivateKey().getQ();

        BigInteger m1 = cipherText.modPow(d, p);
        BigInteger m2 = cipherText.modPow(d, q);

        BigInteger qInv = q.modInverse(p);
        BigInteger h = (m1.subtract(m2)).multiply(qInv).mod(p);
        return m2.add(h.multiply(q));
    }
}