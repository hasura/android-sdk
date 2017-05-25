package io.hasura.sdk.core;

import java.io.IOException;

public interface Converter<T, E extends Exception> {
    T fromResponse(okhttp3.Response r) throws E;

    E fromIOException(IOException e);

    E castException(Exception e);
}
