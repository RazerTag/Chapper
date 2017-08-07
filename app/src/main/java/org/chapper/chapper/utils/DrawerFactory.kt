package org.chapper.chapper.utils

import android.app.Activity
import android.content.Context
import android.view.View
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import org.chapper.chapper.R


object DrawerFactory {
    fun getDrawer(context: Context, name: String, btMacAddress: String): com.mikepenz.materialdrawer.Drawer {
        val headerResult = getHeader(context, name, btMacAddress)

        return DrawerBuilder()
                .withActivity(context as Activity)
                .withAccountHeader(headerResult)
                .withSelectedItem(-1)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(context.getString(R.string.search_for_devices)).withIcon(R.drawable.access_point).withSelectable(false),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName(context.getString(R.string.invite_friends)).withIcon(R.drawable.account_plus).withSelectable(false),
                        PrimaryDrawerItem().withName(context.getString(R.string.settings)).withIcon(R.drawable.settings).withSelectable(false),
                        PrimaryDrawerItem().withName(context.getString(R.string.faq)).withIcon(R.drawable.help_circle).withSelectable(false)
                )
                .withOnDrawerListener(object : com.mikepenz.materialdrawer.Drawer.OnDrawerListener {
                    override fun onDrawerOpened(drawerView: View) {
                        Keyboard.hideKeyboard(context)
                    }

                    override fun onDrawerClosed(drawerView: View) {

                    }

                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                    }
                })
                .build()
    }

    fun getDrawer(context: Context): com.mikepenz.materialdrawer.Drawer {
        return getDrawer(context, "Vladislav Annenkov", "Loading...")
    }

    fun getHeader(context: Context, name: String, btMacAddress: String): AccountHeader {
        return AccountHeaderBuilder()
                .withActivity(context as Activity)
                .withHeaderBackground(R.color.colorSecondary)
                .withSelectionListEnabled(false)
                .withProfileImagesClickable(false)
                .addProfiles(
                        ProfileDrawerItem().withName(name).withEmail(btMacAddress).withIcon(context.getDrawable(R.drawable.menu))
                )
                .build()
    }

    fun getHeader(context: Context): AccountHeader {
        return getHeader(context, "Vladislav Annenkov", "Analise")
    }
}