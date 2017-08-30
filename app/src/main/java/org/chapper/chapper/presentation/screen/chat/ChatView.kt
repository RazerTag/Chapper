package org.chapper.chapper.presentation.screen.chat

interface ChatView {
    fun initChat()
    fun initToolbar()
    fun showMessages()

    fun getChatId(): String

    fun changeMessageList()

    fun startRefreshing()

    fun statusConnected()
    fun statusNearby()
    fun statusOffline()
    fun statusRefresing()
}