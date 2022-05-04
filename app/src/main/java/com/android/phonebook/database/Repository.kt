package com.android.phonebook.database

import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.phonebook.domain.model.ColorModel
import com.android.phonebook.domain.model.PhoneModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(
    private val phoneDao: PhoneDao,
    private val colorDao: ColorDao,
    private val dbMapper: DbMapper
) {
    private val phonesNotInTrashLiveData: MutableLiveData<List<PhoneModel>> by lazy {
        MutableLiveData<List<PhoneModel>>()
    }

    fun getAllPhoneNotInTrash(): LiveData<List<PhoneModel>> = phonesNotInTrashLiveData

    private val phonesInTrashLiveData: MutableLiveData<List<PhoneModel>> by lazy {
        MutableLiveData<List<PhoneModel>>()
    }

    fun getAllPhoneInTrash(): LiveData<List<PhoneModel>> = phonesInTrashLiveData

    init {
        initDatabase(this::updatePhonesLiveData)
    }

    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {

            val colors = ColorDbModel.DEFAULT_COLORS.toTypedArray()
            val dbColors = colorDao.getAllSync()
            if (dbColors.isNullOrEmpty()) {
                colorDao.insertAll(*colors)
            }

            val phones = PhoneDbModel.DEFAULT_PHONES.toTypedArray()
            val dbPhones = phoneDao.getAllSync()
            if (dbPhones.isNullOrEmpty()) {
                phoneDao.insertAll(*phones)
            }

            postInitAction.invoke()
        }
    }

    private fun getAllPhonesDependingOnTrashStateSync(inTrash: Boolean): List<PhoneModel> {
        val colorDbModels: Map<Long, ColorDbModel> =
            colorDao.getAllSync().map { it.id to it }.toMap()
        val dbPhones: List<PhoneDbModel> =
            phoneDao.getAllSync().filter { it.isInTrash == inTrash }.sortedBy { it.first_name }
        return dbMapper.mapPhones(dbPhones, colorDbModels)
    }

    fun insertPhone(phone: PhoneModel) {
        phoneDao.insert(dbMapper.mapDbPhone(phone))
        updatePhonesLiveData()
    }

    fun deletePhones(phoneIds: List<Long>) {
        phoneDao.delete(phoneIds)
        updatePhonesLiveData()
    }

    fun movePhoneToTrash(phoneId: Long) {
        val dbPhone = phoneDao.findByIdSync(phoneId)
        val newDbPhone = dbPhone.copy(isInTrash = true)
        phoneDao.insert(newDbPhone)
        updatePhonesLiveData()
    }

    fun restorePhonesFromTrash(phoneIds: List<Long>) {
        val dbPhonesInTrash = phoneDao.getPhonesByIdsSync(phoneIds)
        dbPhonesInTrash.forEach {
            val newDbPhone = it.copy(isInTrash = false)
            phoneDao.insert(newDbPhone)
        }
        updatePhonesLiveData()
    }

    fun getAllColors(): LiveData<List<ColorModel>> =
        Transformations.map(colorDao.getAll()) { dbMapper.mapColors(it) }

    private fun updatePhonesLiveData() {
        phonesNotInTrashLiveData.postValue(getAllPhonesDependingOnTrashStateSync(false))
        phonesInTrashLiveData.postValue(getAllPhonesDependingOnTrashStateSync(true))
    }

}