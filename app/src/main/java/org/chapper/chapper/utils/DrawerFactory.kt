package org.chapper.chapper.utils

import android.view.View
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import org.chapper.chapper.R
import org.chapper.chapper.screen.dialoglist.DialogListActivity


object DrawerFactory {
    fun getDrawer(activity: DialogListActivity): com.mikepenz.materialdrawer.Drawer {
        return DrawerBuilder()
                .withActivity(activity)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(activity.getString(R.string.search_for_devices)).withIcon(R.drawable.access_point).withSelectable(false),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(activity.getString(R.string.invite_friends)).withIcon(R.drawable.account_plus).withSelectable(false),
                        PrimaryDrawerItem().withName(activity.getString(R.string.settings)).withIcon(R.drawable.settings).withSelectable(false),
                        PrimaryDrawerItem().withName(activity.getString(R.string.faq)).withIcon(R.drawable.help_circle).withSelectable(false)
                )
                .withOnDrawerListener(object : com.mikepenz.materialdrawer.Drawer.OnDrawerListener {
                    override fun onDrawerOpened(drawerView: View) {
                        Keyboard.hideKeyboard(activity)
                    }

                    override fun onDrawerClosed(drawerView: View) {

                    }

                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                    }
                })
                .build()
    }
}