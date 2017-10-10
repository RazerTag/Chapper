package org.chapper.chapper.data.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.database.AppDatabase

@Table(database = AppDatabase::class)
data class Settings(
        @PrimaryKey(autoincrement = true)
        @Unique
        var id: Int = 0,

        @Column
        var isFirstStart: Boolean = true,

        @Column
        var photoId: String = Constants.PHOTO_REQUEST,

        @Column
        var firstName: String = "",

        @Column
        var lastName: String = "",

        @Column
        var isSendByEnter: Boolean = false
)