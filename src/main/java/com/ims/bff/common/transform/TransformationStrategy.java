package com.ims.bff.common.transform;

public interface TransformationStrategy<T, E> {

    T transform(E context);
}
