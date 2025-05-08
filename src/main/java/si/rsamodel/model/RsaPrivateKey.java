package si.rsamodel.model;

import java.math.BigInteger;

public class RsaPrivateKey {
    private final BigInteger inverse;
    private final BigInteger p;
    private final BigInteger q;

    public RsaPrivateKey(BigInteger inverse, BigInteger p, BigInteger q) {
        this.inverse = inverse;
        this.p = p;
        this.q = q;
    }

    public BigInteger getInverse() {
        return inverse;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

}