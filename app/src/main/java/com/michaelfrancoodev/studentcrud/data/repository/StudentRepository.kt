package com.michaelfrancoodev.studentcrud.data.repository

import com.michaelfrancoodev.studentcrud.data.local.dao.StudentDao
import com.michaelfrancoodev.studentcrud.data.local.entity.Student
import kotlinx.coroutines.flow.Flow

/**
 * StudentRepository - Repository layer for Student profile operations
 * Handles business logic and data operations for student profile
 */
class StudentRepository(private val studentDao: StudentDao) {

    // Get profile (reactive)
    fun getProfile(): Flow<Student?> = studentDao.getProfile()

    // Get profile (one-time)
    suspend fun getProfileOnce(): Student? = studentDao.getProfileOnce()

    // Create or update profile
    suspend fun saveProfile(student: Student) {
        studentDao.insertProfile(student)
    }

    // Update entire profile
    suspend fun updateProfile(student: Student) {
        studentDao.updateProfile(student)
    }

    // Update basic information
    suspend fun updateBasicInfo(
        fullName: String,
        university: String,
        major: String,
        semester: String
    ) {
        studentDao.updateBasicInfo(
            fullName = fullName,
            university = university,
            major = major,
            semester = semester
        )
    }

    // Update contact information
    suspend fun updateContactInfo(email: String, phone: String) {
        studentDao.updateContactInfo(email, phone)
    }

    // Update profile photo
    suspend fun updateProfilePhoto(uri: String?) {
        studentDao.updateProfilePhoto(uri)
    }

    // Update study goal
    suspend fun updateStudyGoal(hours: Int) {
        studentDao.updateStudyGoal(hours)
    }

    // Update motto
    suspend fun updateMotto(motto: String) {
        studentDao.updateMotto(motto)
    }

    // Update streak
    suspend fun updateStreak(streak: Int) {
        studentDao.updateStreak(streak)
    }

    // Increment streak by 1
    suspend fun incrementStreak() {
        val current = getProfileOnce()
        current?.let {
            updateStreak(it.currentStreak + 1)
        }
    }

    // Reset streak to 0
    suspend fun resetStreak() {
        updateStreak(0)
    }

    // Add study hours
    suspend fun addStudyHours(hours: Int) {
        studentDao.addStudyHours(hours)
    }

    // Update semester
    suspend fun updateSemester(semester: String) {
        studentDao.updateSemester(semester)
    }

    // Check if profile exists
    suspend fun profileExists(): Boolean {
        return studentDao.profileExists() > 0
    }

    // Check if profile is complete
    suspend fun isProfileComplete(): Boolean {
        val profile = getProfileOnce()
        return profile?.isProfileComplete ?: false
    }

    // Delete profile (for reset/testing)
    suspend fun deleteProfile() {
        studentDao.deleteProfile()
    }

    // Create default profile
    suspend fun createDefaultProfile(fullName: String) {
        val defaultProfile = Student(
            id = 1,
            fullName = fullName,
            dailyStudyGoalHours = 4,
            motto = "Keep learning, keep growing!"
        )
        saveProfile(defaultProfile)
    }
}
