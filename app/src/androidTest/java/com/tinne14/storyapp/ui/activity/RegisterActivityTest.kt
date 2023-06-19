package com.tinne14.storyapp.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tinne14.storyapp.R
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest{
    private val name = "dsgidfgdddadgfhsbna"
    private val email = "justindddgsfhgdsge123@gmail.com"
    private val password = "Justinddhsdgsgsdgse123"

    object EspressoIdlingResource {
        private val RESOURCE = "GLOBAL"
        private val countingIdlingResource = CountingIdlingResource(RESOURCE)

        val idlingresource: IdlingResource
            get() = countingIdlingResource
    }

    @Before
    fun setUp() {
        ActivityScenario.launch(RegisterActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun registerTesting(){
        onView(ViewMatchers.withId(R.id.nameEditText)).perform(ViewActions.typeText(name), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.emailEditText)).perform(ViewActions.typeText(email), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.passwordEditText)).perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.signupButton)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.signupButton)).perform(ViewActions.click())
    }

}