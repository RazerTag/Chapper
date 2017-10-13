package org.chapper.chapper.presentation.screen.intro

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import org.chapper.chapper.R
import org.chapper.chapper.data.repository.SettingsRepository

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
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.light_color)
                .title("Привет.")
                .description("Пропал интернет? Chapper не нуждается в нём.")
                .build())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorSecondary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.garden_gloves_color)
                .title("Работает как рация.\nНо лучше.")
                .description("Общайтесь и координируйтесь — заграницей, на природе или работе — везде, где нет интернета.")
                .build())

        addSlide(SlideFragmentBuilder()
                .neededPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.settings_color)
                .title("Начнём настройку.")
                .description("Для работы приложение будет использовать Bluetooth. И кое-что ещё.")
                .build())

        addSlide(RegisterSlide())

        addSlide(ImagePickSlide())
    }

    override fun onFinish() {
        super.onFinish()

        SettingsRepository.setFirstStart(false)
    }

    override fun onBackPressed() {
        // Nothing
    }
}