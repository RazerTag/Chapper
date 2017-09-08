package org.chapper.chapper.presentation.screen.settings

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import butterknife.bindView
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import de.hdodenhof.circleimageview.CircleImageView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.repository.SettingsRepository
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.verticalLayout
import kotlin.properties.Delegates

class SettingsActivity : AppCompatActivity(), SettingsView {
    private var mPresenter: SettingsPresenter by Delegates.notNull()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)

    private val mPhoto: CircleImageView by bindView(R.id.profilePhoto)
    private val mName: TextView by bindView(R.id.name)
    private val mAddress: TextView by bindView(R.id.address)
    private val mUsername: TextView by bindView(R.id.username)
    private val mSendByEnterSwitch: Switch by bindView(R.id.sendByEnterSwitch)

    private val mUsernameView: View by bindView(R.id.usernameView)

    private var mFlowObserver: FlowContentObserver by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mPresenter = SettingsPresenter(this)

        mPresenter.init(applicationContext)

        mFlowObserver = FlowContentObserver()
        mFlowObserver.registerForContentChanges(applicationContext, Settings::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, Chat::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, Message::class.java)
        mPresenter.databaseChangesListener(applicationContext, mFlowObserver)

        mUsernameView.setOnClickListener {
            alert {
                var username: EditText by Delegates.notNull()

                customView {
                    verticalLayout {
                        setPadding(30, 30, 30, 30)

                        username = editText {
                            setText(SettingsRepository.getUsername())
                            hint = getString(R.string.username)
                        }
                    }

                    positiveButton(getString(R.string.save)) {
                        SettingsRepository.setUsername(username.text.toString())
                    }
                }
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        when (id) {
            R.id.action_change_name -> {
                alert {
                    var firstName: EditText by Delegates.notNull()
                    var lastName: EditText by Delegates.notNull()

                    customView {
                        verticalLayout {
                            setPadding(30, 30, 30, 30)

                            firstName = editText {
                                setText(SettingsRepository.getFirstName())
                                hint = getString(R.string.first_name)
                            }
                            lastName = editText {
                                setText(SettingsRepository.getLastName())
                                hint = getString(R.string.last_name)
                            }
                        }

                        positiveButton(getString(R.string.save)) {
                            SettingsRepository.setFirstName(firstName.text.toString())
                            SettingsRepository.setLastName(lastName.text.toString())
                        }
                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.arrow_left_white)
        mToolbar.setNavigationOnClickListener {
            finishActivity()
        }
    }

    override fun initPhoto(bitmap: Bitmap) {
        mPhoto.setImageBitmap(bitmap)
    }

    override fun initName(text: String) {
        mName.text = text
    }

    override fun initAddress(text: String) {
        mAddress.text = text
    }

    override fun initUsername(text: String) {
        mUsername.text = text
    }

    private fun finishActivity() {
        finish()
    }
}
