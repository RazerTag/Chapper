package org.chapper.chapper.presentation.screen.settings

import android.content.Context
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import org.chapper.chapper.data.repository.SettingsRepository

class SettingsPresenter(private val viewState: SettingsView) {
    fun init(context: Context) {
        viewState.initToolbar()

        val photo = SettingsRepository.getProfilePhoto(context)
        if (photo != null)
            viewState.initPhoto(photo)

        viewState.initName(SettingsRepository.getName())
        viewState.initAddress(SettingsRepository.getAddress(context))
        viewState.initUsername(SettingsRepository.getUsername())

        viewState.setSendByEnter(SettingsRepository.isSendByEnter())
    }

    fun databaseChangesListener(context: Context, observer: FlowContentObserver) {
        observer.addModelChangeListener { _, _, _ ->
            init(context)
        }
    }
}