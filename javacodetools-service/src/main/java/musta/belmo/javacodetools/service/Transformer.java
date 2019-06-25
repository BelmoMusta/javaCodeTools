package musta.belmo.javacodetools.service;

import java.io.IOException;

public interface Transformer<T, R> {
    R transform(T file) throws IOException;
}
