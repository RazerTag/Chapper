package org.chapper.chapper.screen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import io.underdark.Underdark
import io.underdark.transport.TransportKind
import org.chapper.chapper.R
import java.util.*

class MainActivity : AppCompatActivity() {
    var mTextView: TextView = findViewById(R.id.textviw) as TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Underdark.configureTransport(554554345,
                1,
                null, null,
                this,
                EnumSet.of(TransportKind.WIFI,
                        TransportKind.BLUETOOTH))
                .start()

        mTextView.setOnClickListener {

        }
    }
}
