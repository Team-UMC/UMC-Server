package com.umc.networkingService.global.common.base;

public interface EntityLoader<T, ID> {
    T loadEntity(ID id);

    T saveEntity(T t);
}
