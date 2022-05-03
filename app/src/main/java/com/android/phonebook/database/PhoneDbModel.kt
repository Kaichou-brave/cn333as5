package com.android.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "first_name") val first_name: String,
    @ColumnInfo(name = "middle_name") val middle_name: String,
    @ColumnInfo(name = "last_name") val last_name: String,
    @ColumnInfo(name = "contact") val contact: String,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "can_be_checked_off") val canBeCheckedOff: Boolean,
    @ColumnInfo(name = "is_checked_off") val isCheckedOff: Boolean,
    @ColumnInfo(name = "color_id") val colorId: Long,
    @ColumnInfo(name = "is_in_trash") val  isInTrash:Boolean,
) {

    companion object {
        val DEFAULT_PHONES = listOf(
            PhoneDbModel(1,"Robert","Downey","Jr","0001112222","Mobile",false,false,1,false),
            PhoneDbModel(2,"Dwayne","","Johnson","1112223333","Home",false,false,2,false),
            PhoneDbModel(3,"Johnny","","Depp","3334445555","Work",false,false,3,false),
            PhoneDbModel(4,"Brat","","Pitt","4445556666", "Other",false,false,4,false,)
        )
    }
}
