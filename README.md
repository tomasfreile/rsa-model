# RSA Model: Implementación de RSA en Java

Este proyecto es una implementación simple y eficiente del algoritmo RSA en Java. Cubre todo el proceso de generación de claves, encriptación y desencriptación, incluyendo optimizaciones mediante el Teorema de los Restos Chinos (CRT).

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
6. **CRT**: Los valores `p` y `q` se guardan en la clave privada para permitir desencriptado eficiente mediante el Teorema de los Restos Chinos.

## Encriptación y Desencriptación

### Clase `RSA`

La clase `RSA` gestiona el cifrado y descifrado de mensajes usando RSA. El método de descifrado aprovecha el CRT para mayor eficiencia.

```java
public class RSA implements MessageEncryptor<RsaKey> {
    @Override
    public BigInteger encrypt(String message, RsaKey key) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }

        BigInteger n = key.getPublicKey().getModulus();
        BigInteger e = key.getPublicKey().getExponent();
        BigInteger numericMessage = BigIntegerUtil.stringToBigInteger(message);
        BigInteger messageMod = numericMessage.mod(n);
        return messageMod.modPow(e, n);  // Cifrado: c = m^e mod n
    }

    @Override
    public String decrypt(BigInteger cipherText, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();
        BigInteger p = key.getPrivateKey().getP();
        BigInteger q = key.getPrivateKey().getQ();

        BigInteger m1 = cipherText.modPow(d, p);  // c^d mod p
        BigInteger m2 = cipherText.modPow(d, q);  // c^d mod q

        BigInteger qInv = q.modInverse(p);
        BigInteger h = (m1.subtract(m2)).multiply(qInv).mod(p);
        BigInteger m = m2.add(h.multiply(q));  // Reconstrucción vía CRT

        return BigIntegerUtil.bigIntegerToString(m);
    }
}
```

### ¿Por qué se usa el CRT?

El Teorema de los Restos Chinos permite dividir la operación costosa `c^d mod n` en dos operaciones más pequeñas `mod p` y `mod q`, que son más rápidas. Luego se combinan para obtener el resultado final. Esta técnica es estándar en implementaciones profesionales de RSA.

## Ejemplo Completo de Uso

```java
public class Main {
    public static void main(String[] args) {
        // Generar claves
        RSAKeyGenerator keyGen = new RSAKeyGenerator(1024);
        RsaKey key = (RsaKey) keyGen.generateKey();

        // Crear instancia de RSA
        RSA rsa = new RSA();

        String message = "Hello world!";
        BigInteger encrypted = rsa.encrypt(message, key);
        String decrypted = rsa.decrypt(encrypted, key);

        System.out.println("Original: " + message);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
```

## Resumen de componentes matemáticos implementados

- ✅ **Algoritmo extendido de Euclides** para inversa modular.
- ✅ **Exponenciación rápida** (via `BigInteger.modPow`).
- ✅ **Descifrado optimizado** con el Teorema de los Restos Chinos.

---
