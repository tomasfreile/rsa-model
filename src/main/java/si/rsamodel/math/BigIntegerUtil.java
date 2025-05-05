package si.rsamodel.math;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class BigIntegerUtil {
    public static BigInteger stringToBigInteger(String s) {
        return new BigInteger(1, s.getBytes(StandardCharsets.UTF_8));
    }

    public static String bigIntegerToString(BigInteger number) {
        byte[] bytes = number.toByteArray();
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] clean = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, clean, 0, clean.length);
            bytes = clean;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}