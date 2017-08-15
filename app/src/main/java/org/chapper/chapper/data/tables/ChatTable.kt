package org.chapper.chapper.data.tables

import android.content.ContentValues
import android.database.Cursor
import com.google.gson.reflect.TypeToken
import org.chapper.chapper.data.GsonHolder
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.SQLite
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.core.Where
import ru.arturvasilov.sqlite.utils.TableBuilder

class ChatTable : BaseTable<Chat>() {
    companion object {
        val TABLE: Table<Chat> = ChatTable()

        val ID: String = "id"
        val FIRST_NAME: String = "first_name"
        val LAST_NAME: String = "last_name"
        val USERNAME: String = "username"
        val BLUETOOTH_MAC_ADDRESS: String = "bluetooth_mac_address"
        val MESSAGES: String = "messages"

        fun addChat(chat: Chat) {
            SQLite.get().insert(ChatTable.TABLE, chat)
            SQLite.get().notifyTableChanged(SettingsTable.TABLE)
        }

        fun getChat(chatId: String): Chat? = SQLite.get()
                .querySingle(ChatTable.TABLE, Where.create().equalTo(ChatTable.ID, chatId))

        val chats: List<Chat>
            get() = SQLite.get().query(ChatTable.TABLE).reversed()
    }

    override fun onCreate(database: SQLiteDatabase) {
        TableBuilder.create(this)
                .textColumn(ID)
                .textColumn(FIRST_NAME)
                .textColumn(LAST_NAME)
                .textColumn(USERNAME)
                .textColumn(BLUETOOTH_MAC_ADDRESS)
                .textColumn(MESSAGES)
                .execute(database)
    }

    override fun toValues(t: Chat): ContentValues {
        val values = ContentValues()
        values.put(ID, t.id)
        values.put(FIRST_NAME, t.firstName)
        values.put(LAST_NAME, t.lastName)
        values.put(USERNAME, t.username)
        values.put(BLUETOOTH_MAC_ADDRESS, t.bluetoothMacAddress)
        values.put(MESSAGES, GsonHolder.gson.toJson(t.messages))
        return values
    }

    override fun fromCursor(cursor: Cursor): Chat {
        val chat = Chat()

        chat.id = cursor.getString(cursor.getColumnIndex(ID))
        chat.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
        chat.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
        chat.username = cursor.getString(cursor.getColumnIndex(USERNAME))
        chat.bluetoothMacAddress = cursor.getString(cursor.getColumnIndex(BLUETOOTH_MAC_ADDRESS))

        val arrayListType = object : TypeToken<ArrayList<Message>>() {}.type
        chat.messages = GsonHolder.gson.fromJson(cursor.getString(cursor.getColumnIndex(MESSAGES)),
                arrayListType)

        return chat
    }
}