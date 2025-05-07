package si.rsamodel.math;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PrimeUtil {
    private static final SecureRandom random = new SecureRandom();

    public static BigInteger generatePrimeByBits(int bitLength) {
        return BigInteger.probablePrime(bitLength, random);
    }

    public static boolean areCoprime(BigInteger a, BigInteger b) {
        return a.gcd(b).equals(BigInteger.ONE);
    }

    public static BigInteger generateExponent(BigInteger phi) {
        BigInteger candidate;
        do {
            candidate = generatePrimeInRange(BigInteger.TWO, phi.subtract(BigInteger.ONE));
        } while (!areCoprime(candidate, phi));
        return candidate;
    }

    public static BigInteger generatePrimeInRange(BigInteger from, BigInteger to) {
        BigInteger candidate;
        int attempts = 0;
        int maxAttempts = 10000;

        do {
            BigInteger range = to.subtract(from);
            int bits = range.bitLength();
            candidate = new BigInteger(bits, random).add(from);
            attempts++;
        } while ((candidate.compareTo(to) > 0 || !candidate.isProbablePrime(100)) && attempts < maxAttempts);

        if (attempts >= maxAttempts) {
            throw new IllegalStateException("No se encontró un número primo en el rango");
        }

        return candidate;
    }

    public static BigInteger generateEuclideanInverse(BigInteger a, BigInteger b) {
        BigInteger oldR = a, r = b;
        BigInteger oldS = BigInteger.ONE, s = BigInteger.ZERO;

        while (!r.equals(BigInteger.ZERO)) {
            BigInteger quotient = oldR.divide(r);
            BigInteger tempR = r;
            r = oldR.subtract(quotient.multiply(r));
            oldR = tempR;

            BigInteger tempS = s;
            s = oldS.subtract(quotient.multiply(s));
            oldS = tempS;
        }
        return oldS.mod(b);
    }
}