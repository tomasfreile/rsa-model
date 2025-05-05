package si.rsamodel.model;

public class RsaKey implements Key<RsaPublicKey, RsaPrivateKey> {
    private final RsaPublicKey publicKey;
    private final RsaPrivateKey privateKey;

    public RsaKey(RsaPublicKey publicKey, RsaPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public RsaPublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public RsaPrivateKey getPrivateKey() {
        return privateKey;
    }
}