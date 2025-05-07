# RSA Model: Implementación de RSA en Java

Este proyecto es una implementación simple del algoritmo RSA en Java. El modelo cubre todo el proceso de generación de claves, encriptación y desencriptación. A continuación se explica la implementación clave, paso a paso, con ejemplos de código.

## Generación de claves RSA

### Clase RSAKeyGenerator

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
        BigInteger n = p.multiply(q);  // Modulus n (parte de la clave pública)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  // Totiente de n

        BigInteger e = PrimeUtil.generateExponent(phi);  // Exponente público e
        BigInteger d = PrimeUtil.generateEuclideanInverse(e, phi);  // Exponente privado d

        if (d.compareTo(BigInteger.ZERO) < 0) d = d.add(phi);  // Ajuste del inverso modular si es negativo
        else if (d.compareTo(phi) >= 0) d = d.subtract(phi);

        return new RsaKey(new RsaPublicKey(n, e), new RsaPrivateKey(d));  // Devuelve la clave pública y privada
    }
}
```

1. **Generación de primos**: Usamos la función `generatePrimeByBits` para generar dos números primos (p y q).
2. **Cálculo del módulo n**: Se calcula multiplicando p y q, que formarán parte de la clave pública.
3. **Cálculo de φ(n)**: Se calcula el totiente de n como el producto de (p-1) y (q-1).
4. **Generación de exponente e**: Se selecciona un número e que sea coprimo con φ(n).
5. **Generación de exponente privado d**: Calculamos el inverso modular de e respecto a φ(n) usando el algoritmo extendido de Euclides.

## Encriptación y Desencriptación

### Clase RSA

La clase RSA es responsable de la encriptación y desencriptación de los mensajes. Utiliza las claves generadas para procesar los datos de manera segura.

```java
public class RSA implements MessageEncryptor<RsaKey> {
    @Override
    public BigInteger encrypt(String message, RsaKey key) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }

        BigInteger m = key.getPublicKey().getModulus();  // Obtenemos el módulo de la clave pública
        BigInteger e = key.getPublicKey().getExponent();  // Obtenemos el exponente de la clave pública
        BigInteger numericMessage = BigIntegerUtil.stringToBigInteger(message);  // Convertimos el mensaje a un BigInteger
        BigInteger messageMod = numericMessage.mod(m);  // Calculamos el valor del mensaje mod n
        return messageMod.modPow(e, m);  // Aplicamos la fórmula de encriptación: m^e mod n
    }

    @Override
    public String decrypt(BigInteger message, RsaKey key) {
        BigInteger d = key.getPrivateKey().getInverse();  // Obtenemos el exponente privado
        BigInteger m = key.getPublicKey().getModulus();  // Obtenemos el módulo de la clave pública
        BigInteger decrypted = message.modPow(d, m);  // Desencriptamos el mensaje: c^d mod n
        return BigIntegerUtil.bigIntegerToString(decrypted);  // Convertimos el BigInteger de vuelta a texto
    }
}
```

1. **Encriptación**:
    - Convertimos el mensaje a un BigInteger.
    - Aplicamos la fórmula RSA: c = m^e mod n, donde m es el mensaje y e es el exponente público.
2. **Desencriptación**:
    - Usamos el exponente privado d y el módulo n para desencriptar el mensaje: m = c^d mod n.
    - Finalmente, convertimos el BigInteger desencriptado de vuelta a un mensaje legible.

## Ejemplo Completo de Uso

El siguiente ejemplo muestra cómo generar un par de claves, encriptar un mensaje y luego desencriptarlo usando las clases implementadas.

```java
public class Main {
    public static void main(String[] args) {
        // Generamos las claves
        RSAKeyGenerator keyGen = new RSAKeyGenerator(512);  // Tamaño de los primos: 512 bits
        RsaKey key = (RsaKey) keyGen.generateKey();  // Generamos la clave pública y privada

        // Creamos el objeto RSA para encriptar y desencriptar
        RSA rsa = new RSA();

        // Mensaje a encriptar
        String message = "Hello world!";
        
        // Encriptamos el mensaje
        BigInteger encrypted = rsa.encrypt(message, key);  // Mensaje cifrado
        // Desencriptamos el mensaje
        String decrypted = rsa.decrypt(encrypted, key);  // Mensaje original recuperado

        // Imprimimos los resultados
        System.out.println("Original: " + message);  // Mensaje original
        System.out.println("Encrypted: " + encrypted);  // Mensaje encriptado
        System.out.println("Decrypted: " + decrypted);  // Mensaje desencriptado
    }
}
```

**Explicación del flujo**:
1. **Generación de claves**:
    - Se crea una instancia de RSAKeyGenerator con un tamaño de 512 bits para los números primos.
    - Se generan las claves pública y privada usando el método generateKey().
2. **Encriptación**:
    - El mensaje "Hello world!" es convertido a un BigInteger y encriptado usando el exponente público y el módulo de la clave pública.
3. **Desencriptación**:
    - El mensaje encriptado se desencripta usando el exponente privado y el módulo de la clave pública.
4. **Salida**:
    - Se imprime el mensaje original, el mensaje encriptado (en formato BigInteger) y el mensaje desencriptado.

