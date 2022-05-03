package com.android.phonebook.database

import com.android.phonebook.domain.model.ColorModel
import com.android.phonebook.domain.model.NEW_PHONE_ID
import com.android.phonebook.domain.model.PhoneModel
import java.lang.RuntimeException

class DbMapper {
    fun mapPhones(
        phoneDbModels: List<PhoneDbModel>,
        colorDbModels: Map<Long, ColorDbModel>
    ): List<PhoneModel> = phoneDbModels.map {
        val colorDbModel = colorDbModels[it.colorId]
            ?: throw RuntimeException("Color for colorId: ${it.colorId} was not found. Make sure that all colors are passed to this method")

        mapPhone(it, colorDbModel)
    }

    fun mapPhone(phoneDbModel: PhoneDbModel, colorDbModel: ColorDbModel): PhoneModel {
        val color = mapColor(colorDbModel)
        val isCheckedOff = with(phoneDbModel) { if (canBeCheckedOff) isCheckedOff else null }
        return with(phoneDbModel) {
            PhoneModel(
                id,
                first_name,
                middle_name,
                last_name,
                contact,
                tag,
                isCheckedOff,
                color
            )
        }
    }

    fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel> =
        colorDbModels.map { mapColor(it) }

    fun mapColor(colorDbModel: ColorDbModel): ColorModel =
        with(colorDbModel) { ColorModel(id, name, hex) }

    fun mapDbPhone(phone: PhoneModel): PhoneDbModel =
        with(phone) {
            val canBeCheckedOff = isCheckedOff != null
            val isCheckedOff = isCheckedOff ?: false
            if (id == NEW_PHONE_ID)
                PhoneDbModel(
                    first_name = first_name,
                    middle_name = middle_name,
                    last_name = last_name,
                    contact = contact,
                    tag = tag,
                    canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                    colorId = color.id,
                    isInTrash = false
                )
            else
                PhoneDbModel(
                    id,
                    first_name,
                    middle_name,
                    last_name,
                    contact,
                    tag,
                    canBeCheckedOff,
                    isCheckedOff,
                    color.id,
                    false
                )
        }
}