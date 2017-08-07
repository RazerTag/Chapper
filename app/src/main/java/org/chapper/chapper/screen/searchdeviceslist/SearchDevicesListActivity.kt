package org.chapper.chapper.screen.searchdeviceslist

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import org.chapper.chapper.R

class SearchDevicesListActivity : AppCompatActivity() {
    val mToolbar: Toolbar by bindView(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_devices_list)
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.keyboard_backspace)
        mToolbar.setNavigationOnClickListener {
            finishActivity()
        }
    }

    private fun finishActivity() {
        finish()
    }
}
