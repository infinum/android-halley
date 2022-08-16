package com.infinum.halley.core.extensions

import com.infinum.halley.core.serializers.hal.models.HalResource
import java.lang.reflect.Field
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField
import kotlinx.serialization.SerialName

internal fun KProperty1<out HalResource, *>.extractName(): String =
    this.findAnnotation<SerialName>()?.value ?: this.name

internal fun KProperty1<out HalResource, *>.extractValue(value: HalResource): Any? =
    this.getter.call(value)

internal fun KProperty1<out HalResource, *>.setField(resource: HalResource, value: Any?): Unit =
    (this.javaField as Field).let {
        it.isAccessible = true
        it.set(resource, value)
        it.isAccessible = false
    }

internal fun KProperty1<out HalResource, *>.extractClass(): KClass<*> =
    this.javaField?.type?.kotlin!!

internal fun KProperty1<out HalResource, *>.extractType(): Type? =
    this.javaField?.type
