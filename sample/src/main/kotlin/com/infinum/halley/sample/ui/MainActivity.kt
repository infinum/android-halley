// CPD-OFF
package com.infinum.halley.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.link.models.Link
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.databinding.ActivityMainBinding
import com.infinum.halley.retrofit.cache.halleyQueryOptions
import com.infinum.halley.retrofit.cache.halleyTemplateOptions
import com.infinum.halley.sample.data.models.deserialization.AnimalResource
import com.infinum.halley.sample.data.models.deserialization.BlockedResource
import com.infinum.halley.sample.data.models.deserialization.OtherProfileResource
import com.infinum.halley.sample.data.models.deserialization.ProfileResource
import com.infinum.halley.sample.data.models.deserialization.UserResource
import com.infinum.halley.sample.extensions.prettyPrint
import com.infinum.halley.sample.mock.AssetLoader
import com.infinum.halley.sample.mock.server.SampleWebServer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("TooManyFunctions")
class MainActivity : AppCompatActivity() {

    private lateinit var webServer: SampleWebServer

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webServer = SampleWebServer()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webServer.start()

        with(binding) {
            serverSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    webServer.start()
                } else {
                    webServer.stop()
                }
                coreSerialization.isEnabled = isChecked
                coreDeserialization.isEnabled = isChecked
                coreDeserializationWithOptions.isEnabled = isChecked
                retrofit.isEnabled = isChecked
                retrofitCachedParameters.isEnabled = isChecked
                retrofitAnnotatedParameters.isEnabled = isChecked
                retrofitAnnotatedAndCachedParameters.isEnabled = isChecked
                retrofitCoroutines.isEnabled = isChecked
                retrofitCoroutinesCachedParameters.isEnabled = isChecked
                retrofitCoroutinesAnnotatedParameters.isEnabled = isChecked
                retrofitCoroutinesAnnotatedAndCachedParameters.isEnabled = isChecked
                retrofitRxjava.isEnabled = isChecked
                retrofitRxjavaCachedParameters.isEnabled = isChecked
                retrofitRxjavaAnnotatedParameters.isEnabled = isChecked
                retrofitRxjavaAnnotatedAndCachedParameters.isEnabled = isChecked
                ktor.isEnabled = isChecked
                ktorWithParameters.isEnabled = isChecked
            }
            coreSerialization.setOnClickListener { coreSerialization() }
            coreDeserialization.setOnClickListener { coreDeserialization() }
            coreDeserializationWithOptions.setOnClickListener { coreDeserializationWithOptions() }

            retrofit.setOnClickListener { retrofit() }
            retrofitCachedParameters.setOnClickListener { retrofitCachedParameters() }
            retrofitAnnotatedParameters.setOnClickListener { retrofitAnnotatedParameters() }
            retrofitAnnotatedAndCachedParameters.setOnClickListener { retrofitAnnotatedAndCachedParameters() }

            retrofitCoroutines.setOnClickListener { retrofitCoroutines() }
            retrofitCoroutinesCachedParameters.setOnClickListener { retrofitCoroutinesCachedParameters() }
            retrofitCoroutinesAnnotatedParameters.setOnClickListener {
                retrofitCoroutinesAnnotatedParameters()
            }
            retrofitCoroutinesAnnotatedAndCachedParameters.setOnClickListener {
                retrofitCoroutinesAnnotatedAndCachedParameters()
            }

            retrofitRxjava.setOnClickListener { retrofitRxJava() }
            retrofitRxjavaCachedParameters.setOnClickListener { retrofitRxJavaCachedParameters() }
            retrofitRxjavaAnnotatedParameters.setOnClickListener { retrofitRxJavaAnnotatedParameters() }
            retrofitRxjavaAnnotatedAndCachedParameters.setOnClickListener {
                retrofitRxJavaAnnotatedAndCachedParameters()
            }

            ktor.setOnClickListener { ktor() }
            ktorWithParameters.setOnClickListener { ktorWithParameters() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        webServer.stop()
    }

    @Suppress("LongMethod")
    private fun coreSerialization() {
        val halley = Halley(
            configuration = Halley.Configuration(
                prettyPrint = true,
                prettyPrintIndent = "  "
            ),
            httpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                )
                .build()
        )

        val result: String = halley.encodeToString(
            value = ProfileResource(
                self = Link(
                    href = "http://localhost:8080/api/Profile/self"
                ),
                name = "Niko",
                timezone = "Europe/Zagreb",
                blocked = BlockedResource(
                    item = listOf(
                        OtherProfileResource(
                            self = Link(
                                href = "http://localhost:8080/api/Profile/34d43605-5afa-4942-9cd8-469d3bd7d25c"
                            ),
                            name = "Niko",
                            timezone = "Europe/Zagreb",
                            user = UserResource(
                                self = Link(
                                    href = "http://localhost:8080/api/User/self"
                                ),
                                name = "Niko",
                                email = "niko@infinum.com"
                            )
                        ),
                        OtherProfileResource(
                            self = Link(
                                href = "http://localhost:8080/api/Profile/dd67052d-5b89-4d4b-b340-75be68e544d2"
                            ),
                            name = "Marko",
                            timezone = "Europe/Berlin",
                            user = UserResource(
                                self = Link(
                                    href = "http://localhost:8080/api/User/self"
                                ),
                                name = "Niko",
                                email = "niko@infinum.com"
                            )
                        ),
                        OtherProfileResource(
                            self = Link(
                                href = "http://localhost:8080/api/Profile/fc7e5967-4758-4435-9cca-82aa6ec0a0c1"
                            ),
                            name = "Ivan",
                            timezone = "Europe/Berlin",
                            user = UserResource(
                                self = Link(
                                    href = "http://localhost:8080/api/User/self"
                                ),
                                name = "Niko",
                                email = "niko@infinum.com"
                            )
                        )
                    )
                ),
                user = UserResource(
                    self = Link(
                        href = "http://localhost:8080/api/User/self"
                    ),
                    email = "niko@infinum.com",
                    animal = AnimalResource(
                        self = Link(
                            href = "http://localhost:8080/api/Animal/3"
                        ),
                        name = "Freya",
                        age = 8
                    )
                )
            )
        )

        showResult(result)
    }

    private fun coreDeserialization() {
        val halley = Halley(
            configuration = Halley.Configuration(
                prettyPrint = true,
                prettyPrintIndent = "  "
            ),
            httpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                )
                .build()
        )

        val input = AssetLoader.loadFile("profile.json")

        val result: ProfileResource = halley.decodeFromString(
            string = input
        )

        showResult(result.prettyPrint())
    }

    private fun coreDeserializationWithOptions() {
        val halley = Halley(
            configuration = Halley.Configuration(
                prettyPrint = true,
                prettyPrintIndent = "  "
            ),
            httpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                )
                .build()
        )

        val input = AssetLoader.loadFile("profile.json")

        val result: ProfileResource = halley.decodeFromString(
            string = input,
            options = Halley.Options(
                common = Arguments.Common(
                    mapOf(
                        "device" to "Samsung",
                        "rooted" to "false"
                    )
                ),
                query = Arguments.Query(
                    mapOf("animal" to mapOf("country" to "USA"))
                ),
                template = Arguments.Template(
                    mapOf("animal" to mapOf("id" to listOf("1", "2", "3").random()))
                )
            )
        )

        showResult(result.prettyPrint())
    }

    private fun retrofit() {
        webServer.client()?.service?.profile()?.enqueue(object : Callback<ProfileResource> {
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

    private fun retrofitCachedParameters() {
        halleyQueryOptions {
            mapOf("animal" to mapOf("country" to "Brazil"))
        }
        halleyTemplateOptions {
            mapOf("animal" to mapOf("id" to "1"))
        }
        webServer.client()?.service?.profileWithOptionsFromCache()
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

    private fun retrofitAnnotatedParameters() {
        webServer.client()?.service?.profileWithOptionsFromAnnotation()
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

    private fun retrofitAnnotatedAndCachedParameters() {
        halleyQueryOptions {
            mapOf("animal" to mapOf("country" to "Brazil"))
        }
        halleyTemplateOptions {
            mapOf("animal" to mapOf("id" to "1"))
        }
        webServer.client()?.service?.profileWithOptionsFromAnnotationAndCache()
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

    private fun retrofitCoroutines() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showResult(throwable.message.toString())
        }
        MainScope().launch {
            val result: ProfileResource? = withContext(Dispatchers.IO + exceptionHandler) {
                webServer.client()?.serviceCoroutines?.profile()
            }
            result?.let {
                showResult(it.prettyPrint())
            } ?: run {
                showResult("Null object")
            }
        }
    }

    private fun retrofitCoroutinesCachedParameters() {
        halleyQueryOptions {
            mapOf("animal" to mapOf("country" to "Brazil"))
        }
        halleyTemplateOptions {
            mapOf("animal" to mapOf("id" to "1"))
        }
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showResult(throwable.message.toString())
        }
        MainScope().launch {
            val result: ProfileResource? = withContext(Dispatchers.IO + exceptionHandler) {
                webServer.client()?.serviceCoroutines?.profileWithOptionsFromCache()
            }
            result?.let {
                showResult(it.prettyPrint())
            } ?: run {
                showResult("Null object")
            }
        }
    }

    private fun retrofitCoroutinesAnnotatedParameters() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showResult(throwable.message.toString())
        }
        MainScope().launch {
            val result: ProfileResource? = withContext(Dispatchers.IO + exceptionHandler) {
                webServer.client()?.serviceCoroutines?.profileWithOptionsFromAnnotation()
            }
            result?.let {
                showResult(it.prettyPrint())
            } ?: run {
                showResult("Null object")
            }
        }
    }

    private fun retrofitCoroutinesAnnotatedAndCachedParameters() {
        halleyQueryOptions {
            mapOf("animal" to mapOf("country" to "Brazil"))
        }
        halleyTemplateOptions {
            mapOf("animal" to mapOf("id" to "1"))
        }
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showResult(throwable.message.toString())
        }
        MainScope().launch {
            val result: ProfileResource? = withContext(Dispatchers.IO + exceptionHandler) {
                webServer.client()?.serviceCoroutines?.profileWithOptionsFromAnnotationAndCache()
            }
            result?.let {
                showResult(it.prettyPrint())
            } ?: run {
                showResult("Null object")
            }
        }
    }

    private fun retrofitRxJava() {
        webServer.client()?.serviceRx?.profile()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<ProfileResource> {
                override fun onSubscribe(d: Disposable) = Unit

                override fun onSuccess(t: ProfileResource) {
                    showResult(t.prettyPrint())
                }

                override fun onError(e: Throwable) {
                    showResult(e.message.toString())
                }
            })
    }

    private fun retrofitRxJavaCachedParameters() {
        halleyQueryOptions {
            mapOf("animal" to mapOf("country" to "Brazil"))
        }
        halleyTemplateOptions {
            mapOf("animal" to mapOf("id" to "1"))
        }
        webServer.client()?.serviceRx?.profileWithOptionsFromCache()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<ProfileResource> {
                override fun onSubscribe(d: Disposable) = Unit

                override fun onSuccess(t: ProfileResource) {
                    showResult(t.prettyPrint())
                }

                override fun onError(e: Throwable) {
                    showResult(e.toString())
                }
            })
    }

    private fun retrofitRxJavaAnnotatedParameters() {
        webServer.client()?.serviceRx?.profileWithOptionsFromAnnotation()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<ProfileResource> {
                override fun onSubscribe(d: Disposable) = Unit

                override fun onSuccess(t: ProfileResource) {
                    showResult(t.prettyPrint())
                }

                override fun onError(e: Throwable) {
                    showResult(e.toString())
                }
            })
    }

    private fun retrofitRxJavaAnnotatedAndCachedParameters() {
        halleyQueryOptions {
            mapOf("animal" to mapOf("country" to "Brazil"))
        }
        halleyTemplateOptions {
            mapOf("animal" to mapOf("id" to "1"))
        }
        webServer.client()?.serviceRx?.profileWithOptionsFromAnnotationAndCache()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<ProfileResource> {
                override fun onSubscribe(d: Disposable) = Unit

                override fun onSuccess(t: ProfileResource) {
                    showResult(t.prettyPrint())
                }

                override fun onError(e: Throwable) {
                    showResult(e.toString())
                }
            })
    }

    private fun ktor() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showResult(throwable.message.toString())
        }
        MainScope().launch {
            val result: ProfileResource? = withContext(Dispatchers.IO + exceptionHandler) {
                webServer.ktor()?.profile()
            }
            result?.let {
                showResult(it.prettyPrint())
            } ?: run {
                showResult("Null object")
            }
        }
    }

    private fun ktorWithParameters() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showResult(throwable.message.toString())
        }
        MainScope().launch {
            val result: ProfileResource? = withContext(Dispatchers.IO + exceptionHandler) {
                webServer.ktor()?.profileWithOptions()
            }
            result?.let {
                showResult(it.prettyPrint())
            } ?: run {
                showResult("Null object")
            }
        }
    }

    private fun showResult(result: String) {
        ResultCache.setCurrent(result)
        ResultFragment().show(supportFragmentManager, result)
    }
}
// CPD-ON
