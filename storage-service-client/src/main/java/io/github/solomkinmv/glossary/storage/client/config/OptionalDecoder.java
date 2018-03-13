package io.github.solomkinmv.glossary.storage.client.config;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

@AllArgsConstructor
class OptionalDecoder implements Decoder {

    private final Decoder delegate;

    private static boolean isOptional(Type type) {
        if (!(type instanceof ParameterizedType)) return false;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getRawType().equals(Optional.class);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (!isOptional(type)) return delegate.decode(response, type);

        if (response.status() == 404 || response.status() == 204) return Optional.empty();
        Type enclosedType = Util.resolveLastTypeParameter(type, Optional.class);
        return Optional.of(delegate.decode(response, enclosedType));
    }
}