package com.infinum.halley.core.extensions

import com.infinum.halley.core.serializers.hal.models.HalResource
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

internal fun KProperty1<out HalResource, *>.isCollection(): Boolean =
    isList() || isArray() || isSet()

internal fun KProperty1<out HalResource, *>.isList(): Boolean =
    this.javaField?.type?.let { List::class.java.isAssignableFrom(it) } ?: false

internal fun KProperty1<out HalResource, *>.isSet(): Boolean =
    this.javaField?.type?.let { Set::class.java.isAssignableFrom(it) } ?: false

internal fun KProperty1<out HalResource, *>.isArray(): Boolean =
    this.javaField?.type?.let { Array::class.java.isAssignableFrom(it) } ?: false

internal fun KProperty1<out HalResource, *>.extractGenericType(): Type? =
    if (this.isArray()) {
        this.javaField?.type?.componentType
    } else {
        (this.javaField?.genericType as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
    }

internal fun KProperty1<out HalResource, *>.extractGenericClass(): KClass<*>? =
    if (this.isArray()) {
        this.javaField?.type?.componentType?.kotlin
    } else {
        ((this.javaField?.genericType as? ParameterizedType)?.actualTypeArguments?.firstOrNull() as? Class<*>)?.kotlin
    }

internal fun KProperty1<out HalResource, *>.extractCollection(value: HalResource): Any? =
    when {
        this.isArray() -> this.extractValue(value) as Array<*>?
        this.isSet() -> this.extractValue(value) as Set<*>?
        else -> this.extractValue(value) as List<*>?
    }
