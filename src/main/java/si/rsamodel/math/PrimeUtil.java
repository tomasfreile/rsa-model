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
        for (int i = 0; i < 10000; i++) {
            BigInteger candidate = generatePrimeInRange(BigInteger.TWO, phi.subtract(BigInteger.ONE));
            if (areCoprime(candidate, phi)) return candidate;
        }
        throw new IllegalStateException("No se encontró un exponente válido");
    }

    public static BigInteger generatePrimeInRange(BigInteger from, BigInteger to) {
        BigInteger range = to.subtract(from);
        int bits = range.bitLength();
        for (int i = 0; i < 10000; i++) {
            BigInteger candidate = new BigInteger(bits, random).add(from);
            if (candidate.compareTo(to) <= 0 && candidate.isProbablePrime(100)) {
                return candidate;
            }
        }
        throw new IllegalStateException("No se encontró un número primo en el rango");
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
        return oldS;
    }
}