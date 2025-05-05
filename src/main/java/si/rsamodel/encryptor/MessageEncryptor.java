package si.rsamodel.encryptor;

import java.math.BigInteger;

public interface MessageEncryptor<P> {
    BigInteger encrypt(String message, P key);
    String decrypt(BigInteger message, P key);
}