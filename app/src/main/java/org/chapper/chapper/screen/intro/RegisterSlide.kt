package org.chapper.chapper.screen.intro

import agency.tango.materialintroscreen.SlideFragment
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.tables.SettingsTable
import ru.arturvasilov.sqlite.core.SQLite


class RegisterSlide : SlideFragment() {
    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var username: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register_slide, container, false)

        firstName = view.findViewById(R.id.first_name) as EditText?
        lastName = view.findViewById(R.id.last_name) as EditText?
        username = view.findViewById(R.id.username) as EditText?

        username!!.setText(BluetoothAdapter.getDefaultAdapter().name)

        return view
    }

    override fun backgroundColor(): Int {
        return R.color.colorPrimary
    }

    override fun buttonsColor(): Int {
        return R.color.colorAccent
    }

    override fun canMoveFurther(): Boolean {
        return !firstName!!.text.isEmpty()
                && !lastName!!.text.isEmpty()
                && !username!!.text.isEmpty()
    }

    override fun cantMoveFurtherErrorMessage(): String {
        return getString(R.string.fill_in_all_fields)
    }

    override fun onPause() {
        super.onPause()

        saveData()
    }

    private fun saveData() {
        val settings = Settings()
        settings.isFirstStart = false
        settings.firstName = firstName!!.text.toString()
        settings.lastName = lastName!!.text.toString()
        BluetoothAdapter.getDefaultAdapter().name = username!!.text.toString()

        SQLite.get().delete(SettingsTable.TABLE)
        SQLite.get().insert(SettingsTable.TABLE, settings)
        SQLite.get().notifyTableChanged(SettingsTable.TABLE)
    }
}