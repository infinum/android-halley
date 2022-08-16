package com.infinum.halley.core.extensions

import com.infinum.halley.core.annotations.HalLink
import com.infinum.halley.core.serializers.hal.models.HalResource
import java.lang.reflect.Field
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

internal fun KProperty1<out HalResource, *>.isHalLink(): Boolean =
    (this.javaField as Field).getAnnotation(HalLink::class.java) != null
