package com.aykutasil.mockk

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StaffRepositoryTest {

    private lateinit var staffRepository: StaffRepository
    private val staffDao: StaffDao = mockk(relaxed = true)
    private val logUtil: LogUtil = mockk(relaxed = true)


    @Before
    fun setUp() {
        staffRepository = StaffRepository(staffDao, logUtil)
    }

    @Test
    fun `staff should save if it's name is not null`() {
        val staff = Staff(name = "Aykut")
        staffRepository.saveLocalRepo(staff)

        verify(exactly = 1) { staffDao.save() }
        verify(atLeast = 1) { staffDao.save() }
        verify(atMost = 1) { staffDao.save() }

        verify { logUtil.i(any()) }
    }

    @Test
    fun `staff should not save if it's name is null`() {
        val staff = Staff()
        staffRepository.saveLocalRepo(staff)
        verify(exactly = 0) { staffDao.save() }
    }

    @Test
    fun `staff should save if internet connection enable`() {
        mockkStatic(InternetConnectionHelper::class)
        every { InternetConnectionHelper.checkInternet() } returns true
        val staff = Staff(name = "Aykut")
        staffRepository.saveRemoteRepo(staff)
        verify(atLeast = 1) { staffDao.save() }
    }

    @Test
    fun `staff should  not save if internet connection disable`() {
        mockkStatic(InternetConnectionHelper::class)
        every { InternetConnectionHelper.checkInternet() } returns false
        val staff = Staff(name = "Aykut")
        staffRepository.saveRemoteRepo(staff)
        verify(exactly = 0) { staffDao.save() }

        verify { InternetConnectionHelper.checkInternet() }
    }

}