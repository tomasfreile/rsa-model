package si.rsamodel.key;

import si.rsamodel.model.Key;

public interface KeyGenerator<P, S> {
    Key<P, S> generateKey();
}