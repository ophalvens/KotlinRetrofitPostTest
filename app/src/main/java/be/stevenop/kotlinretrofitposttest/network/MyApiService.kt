package be.stevenop.kotlinretrofitposttest.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Vervang dit endpoint door het endpoint van jouw api.
 * In dit geval moet deze URI eindigen met een /
 */
private const val baseUrl = "https://stevenop.be/wm/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(baseUrl)
    .build()

interface MyApiService {
    /**
     * Voor een get request is er niet veel speciaal aan te passen, hier volgen we
     * de cursus.
     * Let wel op : deze api is publiek, studenten kunnen producten verwijderen dus
     * het is mogelijk dat als je dit test, dat je een lege lijst krijgt. Is dat het
     * geval, voeg dan een paar producten toe via de test-app die je eerder al gebruikte.
     */
    @GET("PRODUCTSget.php")
    suspend fun getProducten() : String


    /**
     * Let bij de parameters vooral op wat je wil meegeven :
     * - de case / naam van het field : het deel tussen ("") moet overeenkomen met wat je in de API verwacht
     * - in dit geval verwacht de php api 2 velden : ("name") en ("password")
     */
    @FormUrlEncoded
    @POST("LOGINtest.php")
    suspend fun login(
        @Field("name") name : String ,
        @Field("password") password: String
    ) : String
}

object MyApi {
    val retroFitService : MyApiService by lazy {
        retrofit.create(MyApiService::class.java)
    }
}