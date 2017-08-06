package org.chapper.chapper.screen.dialoglist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import app.akexorcist.bluetotohspp.library.BluetoothState
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.bluetooth.BluetoothFactory

class DialogListActivity : AppCompatActivity() {
    val mTextView: TextView by bindView(R.id.textviw)

    private val bt = BluetoothFactory.getBluetoothSSP(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_list)

        bt.setBluetoothStateListener { state ->
            when (state) {
                BluetoothState.REQUEST_ENABLE_BT -> {
                    mTextView.setText("Bluetooth yoooo")
                }
                BluetoothState.STATE_NONE -> {
                    mTextView.setText("ASKmDKASMdKASMD")
                }
            }
        }
    }

    override fun onResume() {
        super.onStart()

        if (!bt.isBluetoothAvailable) {
            mTextView.setText("Bluetooth break")
        } else if (bt.isBluetoothAvailable) {
            if (!bt.isBluetoothEnabled()) {
                // Do somthing if bluetooth is disable
            } else {
                // Do something if bluetooth is already enable
            }
        }
    }
}
