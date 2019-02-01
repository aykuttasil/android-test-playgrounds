package com.aykutasil.mockitokotlinstatic

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@PrepareForTest(InternetConnectionHelper::class)
@RunWith(PowerMockRunner::class)
class StaffRepositoryTest {

    private lateinit var staffRepository: StaffRepository
    private val staffDao: StaffDao = mock()//PowerMockito.mock(StaffDao::class.java)
    private val logUtil: LogUtil = mock()

    //@get:Rule
    //val powermockRule = PowerMockRule()

    @Before
    fun setUp() {
        staffRepository = StaffRepository(staffDao, logUtil)
    }

    @Test
    fun `staff should save if it's name is not null`() {
        val staff = Staff(name = "Aykut")
        staffRepository.saveLocalRepo(staff)

        verify(staffDao).save()
        verify(staffDao, atLeastOnce()).save()
        verify(staffDao, only()).save()

        verify(logUtil).i(anyString())
    }

    @Test
    fun `staff should not save if it's name is null`() {
        val staff = Staff()
        staffRepository.saveLocalRepo(staff)
        verify(staffDao, never()).save()
    }

    @Test
    fun `staff should save if internet connection enable`() {
        PowerMockito.mockStatic(InternetConnectionHelper::class.java)
        PowerMockito.`when`(InternetConnectionHelper.checkInternet()).thenReturn(true)
        val staff = Staff(name = "Aykut")
        staffRepository.saveRemoteRepo(staff)
        verify(staffDao, atLeastOnce()).save()
    }

    @Test
    fun `staff should  not save if internet connection disable`() {
        PowerMockito.mockStatic(InternetConnectionHelper::class.java)
        PowerMockito.`when`(InternetConnectionHelper.checkInternet()).thenReturn(false)
        val staff = Staff(name = "Aykut")
        staffRepository.saveRemoteRepo(staff)
        verify(staffDao, never()).save()
    }

}