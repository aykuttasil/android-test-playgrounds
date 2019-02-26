package com.aykutasil.mockserver

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

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingResource


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var mIdlingResource: IdlingResource
    private lateinit var mockWebServer: MockWebServer

    //@get:Rule
    //val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            mIdlingResource = activity.idlingResource
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.close()
        IdlingRegistry.getInstance().unregister(mIdlingResource)
    }

    @Test
    fun abc() {
        onView(withId(R.id.btnRequest)).perform(click())
        onView(withId(R.id.txtMsg)).check(matches(withText("Leanne Graham")))
    }
}