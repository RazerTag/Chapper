package org.chapper.chapper.presentation.screen.intro

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import org.chapper.chapper.R


class IntroActivity : MaterialIntroActivity() {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLastSlideAlphaExitTransition(false)
        hideBackButton()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorSecondary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.forum)
                .title("Привет.")
                .description("Это лучший оффлайн мессенджер.")
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorSecondary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.nature_people)
                .title("Работает как рация.\nНо лучше.")
                .description("Общайтесь и координируйтесь — заграницей, на природе или работе — везде, где нет интернета.")
                .build())

        addSlide(SlideFragmentBuilder()
                .neededPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.bluetooth_connect)
                .title("Начнём настройку.")
                .description("Пожалуйста, разрешите приложению использовать ваш Bluetooth.")
                .build())

        addSlide(RegisterSlide())

        addSlide(ImagePickSlide())
    }

    override fun onBackPressed() {
        // Nothing
    }
}