package com.infinum.halley.core.extensions

import com.infinum.halley.core.annotations.HalEmbedded
import com.infinum.halley.core.serializers.hal.models.HalResource
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

internal fun KProperty1<out HalResource, *>.isHalEmbedded(): Boolean =
    this.javaField?.getAnnotation(HalEmbedded::class.java) != null
