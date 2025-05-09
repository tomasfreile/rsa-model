package si.rsamodel.encryptor;

import si.rsamodel.model.RsaKey;

import java.math.BigInteger;

public interface RsaDecryptStrategy {
    BigInteger decrypt(BigInteger cipherText, RsaKey key);
}