# RSA Model: Implementación de RSA en Java

Este proyecto es una implementación simple y eficiente del algoritmo RSA en Java. Cubre todo el proceso de generación de claves, encriptación y desencriptación, incluyendo una optimización opcional mediante el Teorema de los Restos Chinos (CRT).

## Generación de claves RSA

### Clase `RSAKeyGenerator`

El generador de claves RSA crea una clave pública y una clave privada basadas en dos números primos grandes. La clase implementa el proceso matemático detrás de la generación de claves RSA.

```java
public class RSAKeyGenerator implements KeyGenerator<RsaPublicKey, RsaPrivateKey> {
    private final int primeSize;

    public RSAKeyGenerator(int primeSize) {
        this.primeSize = primeSize;
    }

    @Override
    public Key<RsaPublicKey, RsaPrivateKey> generateKey() {
        BigInteger p = PrimeUtil.generatePrimeByBits(primeSize);
        BigInteger q = PrimeUtil.generatePrimeByBits(primeSize);
        BigInteger n = p.multiply(q);  // Modulus n
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  // φ(n)

        BigInteger e = PrimeUtil.generateExponent(phi);  // Exponente público e
        BigInteger d = PrimeUtil.generateEuclideanInverse(e, phi);  // Inverso modular d

        if (d.compareTo(BigInteger.ZERO) < 0) d = d.add(phi);
        else if (d.compareTo(phi) >= 0) d = d.subtract(phi);

        return new RsaKey(new RsaPublicKey(n, e), new RsaPrivateKey(d, p, q));
    }
}
```

### Detalles:
1. **Primos p y q**: Se generan con `generatePrimeByBits`.
2. **Módulo n**: Se calcula como `p * q`.
3. **Totiente φ(n)**: Calculado como `(p - 1)(q - 1)`.
4. **Exponente público e**: Generado como coprimo de φ(n).
5. **Exponente privado d**: Inverso modular de `e mod φ(n)` usando el algoritmo extendido de Euclides.
6. **CRT**: Los valores `p` y `q` se guardan en la clave privada. El descifrado puede utilizar opcionalmente el Teorema de los Restos Chinos.

## Encriptación y Desencriptación

### Clase `RSA`

La clase `RSA` gestiona el cifrado y descifrado de mensajes usando RSA. El método de descifrado puede configurarse para usar o no el CRT.

```java
public class RSA implements MessageEncryptor<RsaKey> {
    private final boolean useChineseRemainder;

    public RSA() {
        this(true); // Usa CRT por defecto
    }

    public RSA(boolean useChineseRemainder) {
        this.useChineseRemainder = useChineseRemainder;
    }

    @Override
    public BigInteger encrypt(String message, RsaKey key) {
        BigInteger n = key.getPublicKey().getModulus();
        BigInteger e = key.getPublicKey().getExponent();
        BigInteger numericMessage = BigIntegerUtil.stringToBigInteger(message);
        return numericMessage.mod(n).modPow(e, n);
    }

    @Override
    public String decrypt(BigInteger cipherText, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();
        BigInteger n = key.getPublicKey().getModulus();

        if (!useChineseRemainder) {
            return BigIntegerUtil.bigIntegerToString(cipherText.modPow(d, n));
        }

        BigInteger p = key.getPrivateKey().getP();
        BigInteger q = key.getPrivateKey().getQ();

        BigInteger m1 = cipherText.modPow(d, p);
        BigInteger m2 = cipherText.modPow(d, q);

        BigInteger qInv = q.modInverse(p);
        BigInteger h = (m1.subtract(m2)).multiply(qInv).mod(p);
        BigInteger m = m2.add(h.multiply(q));

        return BigIntegerUtil.bigIntegerToString(m);
    }
}
```

### Benchmark de Desempeño (2048 bits)

Se compararon los tiempos de descifrado con y sin CRT para un mensaje de prueba:

```
Original: This is a test message for RSA encryption
Encrypted: 45593394... [BigInteger largo]
Decrypted (with CRT): This is a test message for RSA encryption
Decrypted (without CRT): This is a test message for RSA encryption
Time with CRT: 9 ms
Time without CRT: 14 ms
```

> CRT mejora el rendimiento del descifrado, especialmente con claves grandes (2048 bits o más). La diferencia se amplifica cuando se repite el proceso múltiples veces.

## Ejemplo Completo de Uso

```java
public class Main {
    public static void main(String[] args) {
        RSAKeyGenerator keyGen = new RSAKeyGenerator(2048);
        RsaKey key = (RsaKey) keyGen.generateKey();

        String message = "This is a test message for RSA encryption";

        RSA rsaWithCRT = new RSA(true);
        RSA rsaWithoutCRT = new RSA(false);

        BigInteger encrypted = rsaWithCRT.encrypt(message, key);
        String decryptedCRT = rsaWithCRT.decrypt(encrypted, key);
        String decryptedNoCRT = rsaWithoutCRT.decrypt(encrypted, key);

        System.out.println("Original: " + message);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted (with CRT): " + decryptedCRT);
        System.out.println("Decrypted (without CRT): " + decryptedNoCRT);
    }
}
```

## Resumen de componentes matemáticos implementados

- ✅ **Algoritmo extendido de Euclides** para inversa modular.
- ✅ **Exponenciación rápida** (via `BigInteger.modPow`).
- ✅ **Descifrado optimizado** opcional con el Teorema de los Restos Chinos (CRT).

---