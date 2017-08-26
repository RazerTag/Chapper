package org.chapper.chapper.data.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import com.raizlabs.android.dbflow.structure.BaseModel
import org.chapper.chapper.data.ActionType
import org.chapper.chapper.data.database.AppDatabase

@Table(database = AppDatabase::class)
data class AppAction(
        @PrimaryKey(autoincrement = true)
        @Unique
        var id: Int = 0,

        @Column
        var type: ActionType = ActionType.BLUETOOTH_ACTION
) : BaseModel()