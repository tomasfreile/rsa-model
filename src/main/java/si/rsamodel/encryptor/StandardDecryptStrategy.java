package si.rsamodel.encryptor;

import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public class StandardDecryptStrategy implements RsaDecryptStrategy {

    @Override
    public BigInteger decrypt(BigInteger cipherText, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();
        BigInteger n = key.getPublicKey().getModulus();
        return cipherText.modPow(d, n);
    }
}