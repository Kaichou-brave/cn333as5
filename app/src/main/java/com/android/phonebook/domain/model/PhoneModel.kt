package com.android.phonebook.domain.model


const val NEW_PHONE_ID = -1L

data class PhoneModel(
    val id: Long = NEW_PHONE_ID,
    val first_name: String = "",
    val middle_name: String = "",
    val last_name: String = "",
    val contact: String = "",
    val tag: String = "",
    val isCheckedOff: Boolean? = null,
    val color: ColorModel = ColorModel.DEFAULT

)

