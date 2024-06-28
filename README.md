![Download](https://img.shields.io/maven-central/v/com.infinum.halley/halley-core) ![Validate Gradle Wrapper](https://github.com/infinum/android-halley/workflows/Validate%20Gradle%20Wrapper/badge.svg)

### <img align="left" src="logo.svg" width="48">

# Halley

_Halley_ provides a simple way on Android to serialize and deserialize models according
to [JSON Hypertext Application Language specification](https://datatracker.ietf.org/doc/html/draft-kelly-json-hal) also
known just as HAL.  
Besides manual call sites that core package provides, Retrofit and Ktor integrations are included.  
_Halley_ is built on top of [KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization).

## Getting started

There are several ways to include _Halley_ in your project, depending on your use case.  
In every case, you should include and apply _Halley_ plugin first. Plugin will include core dependencies and apply KotlinX Serialization in your project.  
You have to add buildscript dependencies in your project level `build.gradle` or `build.gradle.kts`:

### Core - with Halley plugin

**Groovy**

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
}
```

**KotlinDSL**

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
}
```

To include plugin to your project, you have to add buildscript dependencies in your project level `build.gradle` or `build.gradle.kts`:

**Groovy**
```gradle
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "com.infinum.halley:halley-plugin:0.0.6"
    }
}
```
**KotlinDSL**
```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.infinum.halley:halley-plugin:0.0.6")
    }
}
```

Then apply the plugin in your app `build.gradle` or `build.gradle.kts` :

**Groovy**
```gradle
apply plugin: "com.infinum.halley.plugin"
```
**KotlinDSL**
```kotlin
plugins {
    ...
    
    id("com.infinum.halley.plugin")
}
```

Now you can sync your project and core features like serialization and deserialization will be automatically provided.

### Core - without Halley plugin

**Groovy**

```groovy
implementation "com.infinum.halley:halley-core:0.0.6"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.halley:halley-core:0.0.6")
```

### Retrofit

**Groovy**

```groovy
implementation "com.infinum.halley:halley-retrofit:0.0.6"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.halley:halley-retrofit:0.0.6")
```

### Ktor

**Groovy**

```groovy
implementation "com.infinum.halley:halley-ktor:0.0.6"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.halley:halley-ktor:0.0.6")
```

### Retrofit and Ktor together

**Groovy**

```groovy
implementation "com.infinum.halley:halley-retrofit:0.0.6"
implementation "com.infinum.halley:halley-ktor:0.0.6"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.halley:halley-retrofit:0.0.6")
implementation("com.infinum.halley:halley-ktor:0.0.6")
```

## Usage

### Core
**Serialization**
```kotlin
// create or reuse default Halley instance
val halley = Halley()
// serialize Kotlin class to String
val result: String = halley.encodeToString(
    value = ...
)
```
**Deserialization**
```kotlin
// create or reuse default Halley instance 
val halley = Halley()
// deserialize String to Kotlin class
val actual: HalModel = halley.decodeFromString(
    string = " ... "
)
```

### Retrofit

**Add converter factory**

```kotlin
Retrofit.Builder()
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Optional only if you use RxJava
    // Halley for Retrofit provides an extension on KotlinX Serialization Json 
    .addConverterFactory(Json.asHalleyConverterFactory())
    .addConverterFactory(ScalarsConverterFactory.create())
    .client(httpClient)
    .baseUrl(baseUrl)
    .build()
```
### Ktor
**Deserialization**
```kotlin
HttpClient(CIO) {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTP
            host = "localhost"
        }
        port = 8080
        // Halley for Ktor provides a ContentType header,
        // this is a mandatory line to add for Ktor to resolve objects.
        contentType(ContentType.HAL)
    }
    // Halley for Ktor provides a plugin to install with default configuration
    install(HalleyPlugin) {
        defaultConfiguration()
    }
}
```
# Models
A typical HAL model class prepared for _Halley_ consists of several parts.
```kotlin
@Serializable
data class Model(

    @HalLink
    @SerialName(value = "self")
    val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

    @HalEmbedded
    @SerialName(value = "user")
    val user: OtherModel? = OtherModel(
        self = Link(
            href = "http://localhost:8008/api/User/self"
        )
    )

) : HalResource
```
Going from top to bottom, these classes must obey the following list of rules:
* A class must be annotated by KotlinX Serialization `@Serializable`.
* A class must implement an empty `HalResource` interface. 
* Classes without `HalResource` interface will be treated like a simple plain JSON. 
* It's recommended to annotate each class property with `@SerialName(value = "...")` to avoid possible ProGuard issues.
* HAL _link_ properties must be annotated with `@HalLink` and be `Link` type provided by _Halley_ library.
* HAL _embedded_ properties must be annotated with `@HalEmbedded`
* Objects under `@HalEmbedded` annotated property must also implement `HalResource` interface.

## Comments and limitations
A few notes about `HalResource` models:
* Kotlin `object` classes are not supported in _Halley_.
* Multiple `Link` classes in one property under one `@HalLink` annotation are supported by using `List`, `Iterable`, `Set`, `Collection` interfaces and `Array` class only.
* Multiple embedded classes in one property under one `@HalEmbedded` annotation are supported by using `List`, `Iterable`, `Set`, `Collection` interfaces and `Array` class only.
* If a model class has `@HalEmbedded` annotated property but server response provides partial or no object in JSON, then _link_ from __links_ part of response JSON will be used to request that resource and populate the parent model before returning the complete parent model.

Please refer to more complex variations of HAL model classes in [sample](https://github.com/infinum/android-halley/tree/main/sample/src/main/kotlin/com/infinum/halley/sample/data/models) module or [test source set in core](https://github.com/infinum/android-halley/tree/main/halley-core/src/test/kotlin/com/infinum/halley/core/shared/models) module of this repository.

## Deserialization options
_Halley_ has 2 ways of providing 3 different option arguments.  
Developers can use _imperative_ and _annotated_ way, or both at the same time, to define option arguments depending on integration use case.  
If both ways are used, imperative way will override any existing and same keys in the annotations.  
Option arguments are defined as _common_, _query_ and _template_.
* common - will be appended as HTTP query parameters on **every** link executed by _Halley_
* query - will be appended as HTTP query parameters on **every link with the matching key** executed by _Halley_
* template - will be replaced on **every link with the matching key** executed by _Halley_ according to [RFC 6570](https://datatracker.ietf.org/doc/html/rfc6570) standard

### Core
```kotlin
// create or reuse default Halley instance 
val halley = Halley()
// deserialize String to Kotlin class
val actual: HalModel = halley.decodeFromString(
    string = " ... ",
    options = Halley.Options(
        common = Arguments.Common(
            mapOf(
                "device" to "Samsung",
                "rooted" to "false"
            )
        ),
        query = Arguments.Query(
            mapOf("animal" to mapOf("age" to "10"))
        ),
        template = Arguments.Template(
            mapOf("user" to mapOf("id" to "1"))
        )
    )
)
```
### Retrofit
When using _Halley_ with Retrofit, it is important to ensure that each Retrofit service interface method is annotated with `@HalTag`. 
The value (any `String` you've chosen) set for the tag will later be used to match the method with the corresponding option arguments.
#### Annotated option arguments in Retrofit service interface
```kotlin
@GET("/Profile/self")
@HalCommonArguments(
    arguments = [
        HalArgumentEntry("device", "Alcatel")
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
@HalTag("profileWithAnnotatedOptions")
fun profileWithAnnotatedOptions(): Call<ProfileResource>
```
#### Imperative option arguments before Retrofit method call
When setting options imperatively, it is important to ensure that you have used the same **tag** value as the one set in the Retrofit service interface method you intend to use the options for.
```kotlin
private fun fetchProfile() {
    // Halley for Retrofit provides convenience halleyQueryOptions functions
    halleyQueryOptions(tag = "profileWithImperativeOptions") {
        mapOf("animal" to mapOf("country" to "Brazil"))
    }
    halleyTemplateOptions(tag = "profileWithImperativeOptions") {
        mapOf("animal" to mapOf("id" to "1"))
    }
    webServer.client()?.service?.profileWithImperativeOptions()
        ?.enqueue(object : Callback<ProfileResource> {
            override fun onResponse(
                call: Call<ProfileResource>,
                response: Response<ProfileResource>
            ) {
                response.body()?.let {
                    showResult(it.prettyPrint())
                }
            }

            override fun onFailure(call: Call<ProfileResource>, t: Throwable) {
                showResult(t.message.toString())
            }
        })
}
```
_Halley_ Retrofit converter factory works as a standalone factory or together with Kotlin Coroutines, RxJava1, RxJava2 and RxJava3 factories.  
Various use cases can be examined in the [sample implementation](https://github.com/infinum/android-halley/blob/main/sample/src/main/kotlin/com/infinum/halley/sample/ui/MainActivity.kt).
### Ktor
```kotlin
HttpClient(CIO) {
    defaultRequest {
        ...
        contentType(ContentType.HAL)
        // Halley provides extension methods for DefaultRequest
        halOptions(
            common = Arguments.Common(
                mapOf(
                    "device" to "Motorola",
                    "rooted" to "false"
                )
            ),
            query = Arguments.Query(
                mapOf("animal" to mapOf("country" to "Germany"))
            ),
            template = Arguments.Template(
                mapOf("animal" to mapOf("id" to "2"))
            )
        )
    }
}

...

suspend fun profileWithOptions(): ProfileResource =
    client.get {
        url {
            path("api", "Profile", "self")
        }
        // Halley provides extension methods for HttpRequest
        halOptions(
            query = Arguments.Query(
                mapOf("animal" to mapOf("country" to "Italy"))
            ),
            template = Arguments.Template(
                mapOf("animal" to mapOf("id" to "1"))
            )
        )
    }.body()
```
An example of a Ktor client can be examined [here](https://github.com/infinum/android-halley/blob/main/sample/src/main/kotlin/com/infinum/halley/sample/mock/client/SampleKtor.kt).
## Requirements
_Halley_ is written entirely in Kotlin, for Kotlin models and projects.

## License

```
Copyright 2022 Infinum

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Credits

Maintained and sponsored by [Infinum](http://www.infinum.com).

<p align="center">
  <a href='https://infinum.com'>
    <picture>
        <source srcset="https://assets.infinum.com/brand/logo/static/white.svg" media="(prefers-color-scheme: dark)">
        <img src="https://assets.infinum.com/brand/logo/static/default.svg">
    </picture>
  </a>
</p>
