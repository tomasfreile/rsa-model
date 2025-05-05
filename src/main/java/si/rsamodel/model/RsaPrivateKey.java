package si.rsamodel.model;

import java.math.BigInteger;

public class RsaPrivateKey {
    private final BigInteger inverse;

    public RsaPrivateKey(BigInteger inverse) {
        this.inverse = inverse;
    }

    public BigInteger getInverse() {
        return inverse;
    }
}