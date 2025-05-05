package si.rsamodel.model;

public interface Key<P, S> {
    P getPublicKey();
    S getPrivateKey();
}