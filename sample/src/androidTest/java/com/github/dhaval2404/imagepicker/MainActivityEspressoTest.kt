package com.github.dhaval2404.imagepicker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.github.dhaval2404.imagepicker.sample.MainActivity
import com.github.dhaval2404.imagepicker.sample.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Test
    fun ensureButtonDisableAfterOneClick() {
        onView(withId(R.id.fab_add_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_gallery_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_camera_photo)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()))
    }
}
