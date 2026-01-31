package com.michaelfrancoodev.studentcrud.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michaelfrancoodev.studentcrud.data.local.entity.Student
import kotlinx.coroutines.flow.Flow

/**
 * StudentDao - Data Access Object for Student profile
 * Single user app - only one profile exists (id = 1)
 */
@Dao
interface StudentDao {

    // Get profile (reactive)
    @Query("SELECT * FROM student_profile WHERE id = 1")
    fun getProfile(): Flow<Student?>

    // Get profile (one-time)
    @Query("SELECT * FROM student_profile WHERE id = 1")
    suspend fun getProfileOnce(): Student?

    // Insert or replace profile
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(student: Student)

    // Update profile
    @Update
    suspend fun updateProfile(student: Student)

    // Update specific fields
    @Query("""
        UPDATE student_profile 
        SET fullName = :fullName,
            university = :university,
            major = :major,
            currentSemester = :semester,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateBasicInfo(
        fullName: String,
        university: String,
        major: String,
        semester: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET email = :email,
            phoneNumber = :phone,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateContactInfo(
        email: String,
        phone: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET profilePhotoUri = :uri,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateProfilePhoto(
        uri: String?,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET dailyStudyGoalHours = :hours,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateStudyGoal(
        hours: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET motto = :motto,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateMotto(
        motto: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET currentStreak = :streak,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateStreak(
        streak: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET totalStudyHours = totalStudyHours + :hours,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun addStudyHours(
        hours: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE student_profile 
        SET currentSemester = :semester,
            updatedAt = :timestamp
        WHERE id = 1
    """)
    suspend fun updateSemester(
        semester: String,
        timestamp: Long = System.currentTimeMillis()
    )

    // Check if profile exists
    @Query("SELECT COUNT(*) FROM student_profile WHERE id = 1")
    suspend fun profileExists(): Int

    // Delete profile (for testing/reset)
    @Query("DELETE FROM student_profile WHERE id = 1")
    suspend fun deleteProfile()
}
