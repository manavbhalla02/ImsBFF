package com.ims.bff.common.transform;

import com.ims.bff.common.component.SupportsType;

public interface TypedTransformationStrategy<K, T, E> extends TransformationStrategy<T, E>, SupportsType<K> {
}
