package com.aykutasil.mockserver

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import java.io.File

import java.io.BufferedReader

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var mIdlingResource: IdlingResource
    private lateinit var mockWebServer: MockWebServer

    private val activityRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java, false, false) {
        override fun afterActivityLaunched() {
            super.afterActivityLaunched()

            // Activity çalışmaya başladıktan sonra içerisindeki nesnelere erişim sağlayabiliriz.
            // Activity içerisinde oluşturulan CountingIdlingResource nesnesine erişerek, asenkron süreçlerin tamamlanıp tamamlanmadığını kontrol edebiliriz.
            mIdlingResource = activity.idlingResource

            // Espresso'ya IdlingResource nesnesi vererek eğer asenkron bir süreç var ise beklemesini ve bittikten sonra devam etmesini söylüyoruz.
            // Espresso'nun Hangi durumda bekleyip hangi durumda devam edeceğini MainActivity içerisinde
            // CountingIdlingResource nesnesinin increment() veya decrememt() fonksiyonlarını çağırarak belirtiyoruz.
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Before
    fun setUp() {
        // Her test fonkysiyonu öncesinde MockWebServer'ı başlatıyoruz.(8080 portundan. Başka port numarası girilebilir.)
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        val mockWebServerUrl = mockWebServer.url("/").toString()
        println("mockWebServerUrl:$mockWebServerUrl")

        // Retrofit'i build etme sırasında baseUrl() olarak MockWebServer'ın adresini veriyoruz. Bu sayede yapılan tüm istekler MockWebServer üzerinden karşılanmış olacak.
        MainActivity.BASE_URL = mockWebServerUrl

        // Tüm ayarlamalar sonrası MainActivity'i başlatıyoruz.
        activityRule.launchActivity(Intent())
    }

    @After
    fun tearDown() {
        // Her test sonrası MockWebServer kapatıyoruz ce IdlingResource nesnemizi unregister ediyoruz.

        mockWebServer.close()
        IdlingRegistry.getInstance().unregister(mIdlingResource)
    }

    @Test
    fun abcd() {
        // Yapılmak istenen request sonrası dönmesini istediğimiz response değerlerini ve server ile ilgili tüm durumları simüle ediyoruz.
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                RestServiceTestHelper.getStringFromFile(
                    InstrumentationRegistry.getInstrumentation().context,
                    "user.json"
                )
            ) // androidTest > assets klasörü altındaki response body değeri olarak dönmesini istediğimiz json dosyalarını okuyoruz.
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.btnRequest)).perform(click())
        onView(withId(R.id.txtMsg)).check(matches(withText("Aykut Asil")))
    }
}

object RestServiceTestHelper {


    @Throws(Exception::class)
    fun getStringFromFile(context: Context, filePath: String): String {
        val stream = context.resources.assets.open(filePath)

        val ret = stream.bufferedReader().use(BufferedReader::readText)
        //Make sure you close all streams.
        stream.close()
        return ret
    }
}