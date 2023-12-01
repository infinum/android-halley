// CPD-OFF
package com.infinum.halley.sample.mock.client.services

import com.infinum.halley.retrofit.annotations.HalArgumentEntry
import com.infinum.halley.retrofit.annotations.HalCommonArguments
import com.infinum.halley.retrofit.annotations.HalQueryArgument
import com.infinum.halley.retrofit.annotations.HalQueryArguments
import com.infinum.halley.retrofit.annotations.HalTag
import com.infinum.halley.retrofit.annotations.HalTemplateArgument
import com.infinum.halley.retrofit.annotations.HalTemplateArguments
import com.infinum.halley.sample.data.models.deserialization.ProfileResource
import retrofit2.http.GET

interface SampleCoroutinesService {

    @GET("/Profile/self")
    @HalTag("profileCoroutines")
    suspend fun profile(): ProfileResource

    @GET("/Profile/self")
    @HalQueryArguments(key = "animal")
    @HalTemplateArguments(key = "animal")
    @HalTag("profileWithOptionsFromCacheCoroutines")
    suspend fun profileWithOptionsFromCache(): ProfileResource

    @GET("/Profile/self")
    @HalCommonArguments(
        arguments = [
            HalArgumentEntry("device", "Xiaomi")
        ]
    )
    @HalQueryArguments(
        arguments = [
            HalQueryArgument(
                "animal",
                [
                    HalArgumentEntry("country", "France")
                ]
            )
        ]
    )
    @HalTemplateArguments(
        arguments = [
            HalTemplateArgument(
                "animal",
                [
                    HalArgumentEntry("id", "1")
                ]
            )
        ]
    )
    @HalTag("profileWithOptionsFromAnnotationCoroutines")
    suspend fun profileWithOptionsFromAnnotation(): ProfileResource

    @GET("/Profile/self")
    @HalQueryArguments(
        arguments = [
            HalQueryArgument(
                "animal",
                [
                    HalArgumentEntry("country", "France")
                ]
            )
        ]
    )
    @HalTemplateArguments(
        arguments = [
            HalTemplateArgument(
                "animal",
                [
                    HalArgumentEntry("id", "1")
                ]
            )
        ]
    )
    @HalTag("profileWithOptionsFromAnnotationAndCacheCoroutines")
    suspend fun profileWithOptionsFromAnnotationAndCache(): ProfileResource
}
// CPD-ON
