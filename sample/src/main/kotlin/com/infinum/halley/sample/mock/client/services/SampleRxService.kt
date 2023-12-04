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
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface SampleRxService {

    @GET("/Profile/self")
    @HalTag("profileRx")
    fun profile(): Single<ProfileResource>

    @GET("/Profile/self")
    @HalQueryArguments(key = "animal")
    @HalTemplateArguments(key = "animal")
    @HalTag("profileWithImperativeOptionsRx")
    fun profileWithImperativeOptions(): Single<ProfileResource>

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
                    HalArgumentEntry("id", "2")
                ]
            )
        ]
    )
    @HalTag("profileWithAnnotatedOptionsRx")
    fun profileWithAnnotatedOptions(): Single<ProfileResource>

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
    @HalTag("profileWithAnnotatedAndImperativeOptionsRx")
    fun profileWithAnnotatedAndImperativeOptions(): Single<ProfileResource>
}
// CPD-ON
