package org.chapper.chapper.presentation.screen.chat

interface ChatView {
    fun initToolbar()
    fun showMessages()

    fun changeMessageList()

    fun startRefreshing()

    fun statusConnected()
    fun statusNearby()
    fun statusOffline()
}