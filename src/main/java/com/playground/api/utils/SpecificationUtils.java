package com.playground.api.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public class SpecificationUtils {
    public static <T, E> Specification<E> optional(T value, Function<T, Specification<E>> specFn) {
        return value == null ? null : specFn.apply(value);
    }
}
