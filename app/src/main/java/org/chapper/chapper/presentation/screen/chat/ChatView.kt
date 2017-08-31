package org.chapper.chapper.presentation.screen.chat

interface ChatView {
    fun initToolbar()
    fun showMessages()

    fun changeMessageList()

    fun sendMessage()

    fun startRefreshing()

    fun statusConnected()
    fun statusNearby()
    fun statusOffline()

    fun isCoarseLocationPermissionDenied(): Boolean
    fun requestCoarseLocationPermission()
}