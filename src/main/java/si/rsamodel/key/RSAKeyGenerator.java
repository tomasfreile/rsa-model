package si.rsamodel.key;

import si.rsamodel.math.PrimeUtil;
import si.rsamodel.model.*;

import java.math.BigInteger;

public class RSAKeyGenerator implements KeyGenerator<RsaPublicKey, RsaPrivateKey> {
    private final int primeSize;

    public RSAKeyGenerator(int primeSize) {
        this.primeSize = primeSize;
    }

    @Override
    public Key<RsaPublicKey, RsaPrivateKey> generateKey() {
        BigInteger p = PrimeUtil.generatePrimeByBits(primeSize);
        BigInteger q = PrimeUtil.generatePrimeByBits(primeSize);
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = PrimeUtil.generateExponent(phi);
        BigInteger d = PrimeUtil.generateEuclideanInverse(e, phi);

        if (d.compareTo(BigInteger.ZERO) < 0) d = d.add(phi);
        else if (d.compareTo(phi) >= 0) d = d.subtract(phi);

        return new RsaKey(new RsaPublicKey(n, e), new RsaPrivateKey(d));
    }
}