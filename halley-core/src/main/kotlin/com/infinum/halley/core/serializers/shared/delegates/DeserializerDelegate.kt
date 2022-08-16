package com.infinum.halley.core.serializers.shared.delegates

internal interface DeserializerDelegate {

    fun decode()

    fun decodeCollection()

    fun decodeObject()
}
