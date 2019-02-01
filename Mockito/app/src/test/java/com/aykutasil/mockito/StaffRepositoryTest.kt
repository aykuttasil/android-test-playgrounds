package com.aykutasil.mockito

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class StaffRepositoryTest {

    private lateinit var staffRepository: StaffRepository
    private val staffDao: StaffDao = mock()
    private val logUtil: LogUtil = mock()

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
    
}