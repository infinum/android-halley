package com.infinum.halley.core.serializers.embedded.models.relationship

import com.infinum.halley.core.Halley

internal sealed interface RelationshipRequest {

    data class Single(
        val name: String,
        val url: String,
        val templated: Boolean,
        val options: Halley.Options?
    ) : RelationshipRequest

    data class Multiple(
        val requests: List<Single>
    ) : RelationshipRequest {

        private val validParametersPredicate: (Single) -> Boolean = { request ->
            when {
                request.templated -> request.options != null
                else -> true
            }
        }

        val validParametersSize = requests.filter(validParametersPredicate).size
    }
}
