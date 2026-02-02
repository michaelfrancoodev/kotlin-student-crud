package com.michaelfrancoodev.studentcrud.data.repository

import com.michaelfrancoodev.studentcrud.data.local.dao.CourseDao
import com.michaelfrancoodev.studentcrud.data.local.entity.Course
import kotlinx.coroutines.flow.Flow

/**
 * CourseRepository - Repository layer for Course operations
 * Handles business logic and data operations for courses
 */
class CourseRepository(private val courseDao: CourseDao) {

    // Get all courses (reactive)
    fun getAllCourses(): Flow<List<Course>> = courseDao.getAllCourses()

    // Get all courses (one-time)
    suspend fun getAllCoursesOnce(): List<Course> = courseDao.getAllCoursesOnce()

    // Get course by id (reactive)
    fun getCourseById(courseId: Int): Flow<Course?> = courseDao.getCourseById(courseId)

    // Get course by id (one-time)
    suspend fun getCourseByIdOnce(courseId: Int): Course? = courseDao.getCourseByIdOnce(courseId)

    // Get active courses only
    fun getActiveCourses(): Flow<List<Course>> = courseDao.getActiveCourses()

    // Get courses by semester
    fun getCoursesBySemester(semester: String): Flow<List<Course>> =
        courseDao.getCoursesBySemester(semester)

    // Get courses by semester (one-time)
    suspend fun getCoursesBySemesterOnce(semester: String): List<Course> =
        courseDao.getCoursesBySemesterOnce(semester)

    // Insert course
    suspend fun insertCourse(course: Course): Long = courseDao.insertCourse(course)

    // Insert multiple courses
    suspend fun insertCourses(courses: List<Course>) = courseDao.insertCourses(courses)

    // Update course
    suspend fun updateCourse(course: Course) = courseDao.updateCourse(course)

    // Delete course
    suspend fun deleteCourse(course: Course) = courseDao.deleteCourse(course)

    // Delete course by id
    suspend fun deleteCourseById(courseId: Int) = courseDao.deleteCourseById(courseId)

    // Archive course (soft delete)
    suspend fun archiveCourse(courseId: Int) = courseDao.archiveCourse(courseId)

    // Unarchive course
    suspend fun unarchiveCourse(courseId: Int) = courseDao.unarchiveCourse(courseId)

    // Get total credits for semester
    suspend fun getTotalCreditsBySemester(semester: String): Int =
        courseDao.getTotalCreditsBySemester(semester)

    // Get total credits for all active courses
    suspend fun getTotalActiveCredits(): Int = courseDao.getTotalActiveCredits()

    // Get course count by semester
    suspend fun getCourseCountBySemester(semester: String): Int =
        courseDao.getCourseCountBySemester(semester)

    // Get total active course count
    suspend fun getActiveCourseCount(): Int = courseDao.getActiveCourseCount()

    // Search courses
    fun searchCourses(query: String): Flow<List<Course>> = courseDao.searchCourses(query)

    // Get courses by instructor
    fun getCoursesByInstructor(instructor: String): Flow<List<Course>> =
        courseDao.getCoursesByInstructor(instructor)

    // Check if course code exists
    suspend fun courseCodeExists(code: String): Boolean =
        courseDao.courseCodeExists(code) > 0

    // Get total recommended study hours for semester
    suspend fun getRecommendedStudyHours(semester: String): Int {
        val courses = getCoursesBySemesterOnce(semester)
        return courses.sumOf { it.recommendedStudyHours }
    }

    // Get total class hours per week for semester
    suspend fun getTotalClassHours(semester: String): Int {
        val courses = getCoursesBySemesterOnce(semester)
        return courses.sumOf { it.credits }
    }

    // Validate course before saving
    suspend fun validateAndSaveCourse(course: Course): Result<Long> {
        return try {
            // Check if course code already exists (except for updates)
            if (course.id == 0) {
                if (courseCodeExists(course.courseCode)) {
                    return Result.failure(Exception("Course code ${course.courseCode} already exists"))
                }
            }

            // Validate credits
            if (course.credits <= 0) {
                return Result.failure(Exception("Credits must be greater than 0"))
            }

            // Validate semester
            if (course.semester.isBlank()) {
                return Result.failure(Exception("Semester cannot be empty"))
            }

            val id = insertCourse(course)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete all courses (for testing/reset)
    suspend fun deleteAllCourses() = courseDao.deleteAllCourses()
}
