package org.chapper.chapper.screen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import io.underdark.Underdark
import io.underdark.transport.TransportKind
import io.underdark.util.Identity
import org.chapper.chapper.R
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mTextView: TextView = findViewById(R.id.textviw) as TextView

        val kek = Underdark.configureTransport(554554345,
                Identity.generateNodeId(), null, null,
                this,
                EnumSet.of(TransportKind.WIFI,
                        TransportKind.BLUETOOTH))

        kek.start()

        mTextView.setOnClickListener {

        }
    }
}
