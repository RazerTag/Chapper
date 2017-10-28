package org.chapper.chapper.presentation.screen.chat

interface ChatView {
    var isForeground: Boolean

    fun initToolbar()
    fun showMessages()

    fun changeMessageList()

    fun sendMessage()
    fun attachContent()

    fun showRefresher()
    fun hideRefresher()

    fun startRefreshing()

    fun statusTyping()
    fun statusConnected()
    fun statusNearby()
    fun statusOffline()

    fun isCoarseLocationPermissionDenied(): Boolean
    fun requestCoarseLocationPermission()
}