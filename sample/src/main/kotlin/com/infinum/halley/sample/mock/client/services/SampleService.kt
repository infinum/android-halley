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
import retrofit2.Call
import retrofit2.http.GET

interface SampleService {

    @GET("/Profile/self")
    @HalTag("profileRetrofit")
    fun profile(): Call<ProfileResource>

    @GET("/Profile/self")
    @HalQueryArguments(key = "animal")
    @HalTemplateArguments(key = "animal")
    @HalTag("profileWithOptionsFromCacheRetrofit")
    fun profileWithOptionsFromCache(): Call<ProfileResource>

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
    @HalTag("profileWithOptionsFromAnnotationRetrofit")
    fun profileWithOptionsFromAnnotation(): Call<ProfileResource>

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
                    HalArgumentEntry("id", "2")
                ]
            )
        ]
    )
    @HalTag("profileWithOptionsFromAnnotationAndCacheRetrofit")
    fun profileWithOptionsFromAnnotationAndCache(): Call<ProfileResource>
}
// CPD-ON
