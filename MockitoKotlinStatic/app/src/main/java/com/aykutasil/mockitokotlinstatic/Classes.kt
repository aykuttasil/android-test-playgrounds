package com.aykutasil.mockitokotlinstatic

import android.util.Log

object LogUtil {
    private val TAG = LogUtil::class.java.simpleName
    fun i(msg: String) {
        Log.i(TAG, msg)
    }
}

object InternetConnectionHelper {
    fun checkInternet(): Boolean {
        return true
    }
}

data class Staff(
    val name: String? = null,
    val surname: String? = null
)

class StaffDao(
    private val staff: Staff,
    private val log: LogUtil
) {
    fun save() {
        log.i("${staff.name} was saved.")
    }

    fun update() {
        log.i("${staff.name} was updated.")
    }

    fun delete() {
        log.i("${staff.name} was deleted.")
    }
}

class StaffRepository(
    private val staffDao: StaffDao,
    private val log: LogUtil
) {
    fun saveLocalRepo(staff: Staff?) {
        staff?.name?.let {
            staffDao.save()
            log.i("saveLocalRepo process is OK")
        }
    }

    fun saveRemoteRepo(staff: Staff?) {
        if (InternetConnectionHelper.checkInternet()) {
            staff?.name?.let {
                staffDao.save()
                log.i("saveRemoteRepo process is OK")
            }
        }
    }
}

