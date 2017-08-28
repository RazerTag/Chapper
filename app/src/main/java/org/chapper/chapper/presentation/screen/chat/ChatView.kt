package org.chapper.chapper.presentation.screen.chat

interface ChatView {
    fun initChat()
    fun initToolbar()
    fun showMessages()

    fun changeMessageList()
}