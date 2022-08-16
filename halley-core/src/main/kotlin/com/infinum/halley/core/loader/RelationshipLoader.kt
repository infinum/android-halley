package com.infinum.halley.core.loader

import com.infinum.halley.core.serializers.embedded.models.relationship.RelationshipRequest
import com.infinum.halley.core.serializers.embedded.models.relationship.RelationshipResponseHolder

internal interface RelationshipLoader {

    fun load(request: RelationshipRequest.Single): RelationshipResponseHolder?

    fun load(requests: RelationshipRequest.Multiple): List<RelationshipResponseHolder>
}
