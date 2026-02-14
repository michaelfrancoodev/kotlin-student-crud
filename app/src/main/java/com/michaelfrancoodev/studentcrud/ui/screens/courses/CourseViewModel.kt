package com.michaelfrancoodev.studentcrud.ui.screens.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import com.michaelfrancoodev.studentcrud.data.local.entity.Course
import com.michaelfrancoodev.studentcrud.data.repository.CourseRepository
import com.michaelfrancoodev.studentcrud.data.repository.ScheduleRepository
import com.michaelfrancoodev.studentcrud.data.repository.StudentRepository
import com.michaelfrancoodev.studentcrud.data.repository.TaskRepository
import com.michaelfrancoodev.studentcrud.domain.model.CourseWithSchedules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * CourseViewModel - Manages courses screen state and operations
 */
class CourseViewModel(
    private val courseRepository: CourseRepository,
    private val scheduleRepository: ScheduleRepository,
    private val studentRepository: StudentRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<CourseDetailUiState>(CourseDetailUiState.Loading)
    val detailUiState: StateFlow<CourseDetailUiState> = _detailUiState.asStateFlow()

    init {
        loadCourses()
    }

    /**
     * Load all courses
     */
    fun loadCourses() {
        viewModelScope.launch {
            try {
                _uiState.value = CourseUiState.Loading

                combine(
                    courseRepository.getActiveCourses(),
                    studentRepository.getProfile()
                ) { courses, student ->
                    val currentSemester = student?.currentSemester ?: "Semester 1"

                    // Filter courses by current semester
                    val semesterCourses = courses.filter { it.semester == currentSemester }

                    // Get total stats
                    val totalCredits = courseRepository.getTotalCreditsBySemester(currentSemester)

                    CourseUiState.Success(
                        courses = semesterCourses,
                        totalCourses = semesterCourses.size,
                        totalCredits = totalCredits,
                        currentSemester = currentSemester
                    )
                }.collect { state ->
                    _uiState.value = state
                }

            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(
                    message = e.message ?: "Failed to load courses"
                )
            }
        }
    }

    /**
     * Load course detail by ID
     */
    fun loadCourseDetail(courseId: Int) {
        viewModelScope.launch {
            try {
                _detailUiState.value = CourseDetailUiState.Loading

                combine(
                    courseRepository.getCourseById(courseId),
                    scheduleRepository.getSchedulesByCourse(courseId)
                ) { course, schedules ->
                    if (course == null) {
                        CourseDetailUiState.Error("Course not found")
                    } else {
                        // Get task count for this course
                        val taskCount = taskRepository.getPendingTaskCountByCourse(courseId)

                        CourseDetailUiState.Success(
                            courseWithSchedules = CourseWithSchedules(
                                course = course,
                                schedules = schedules
                            ),
                            pendingTaskCount = taskCount
                        )
                    }
                }.collect { state ->
                    _detailUiState.value = state
                }

            } catch (e: Exception) {
                _detailUiState.value = CourseDetailUiState.Error(
                    message = e.message ?: "Failed to load course details"
                )
            }
        }
    }

    /**
     * Add or update course
     */
    fun saveCourse(course: Course, schedules: List<ClassSchedule> = emptyList()) {
        viewModelScope.launch {
            try {
                // Save course
                val result = courseRepository.validateAndSaveCourse(course)

                if (result.isSuccess) {
                    val courseId = result.getOrThrow()

                    // Save schedules if provided
                    if (schedules.isNotEmpty()) {
                        scheduleRepository.saveCourseSchedules(courseId.toInt(), schedules)
                    }

                    // Reload courses
                    loadCourses()
                } else {
                    _uiState.value = CourseUiState.Error(
                        message = result.exceptionOrNull()?.message ?: "Failed to save course"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(
                    message = e.message ?: "Failed to save course"
                )
            }
        }
    }

    /**
     * Delete course
     */
    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                courseRepository.deleteCourseById(courseId)
                loadCourses()
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(
                    message = e.message ?: "Failed to delete course"
                )
            }
        }
    }

    /**
     * Archive course
     */
    fun archiveCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                courseRepository.archiveCourse(courseId)
                loadCourses()
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(
                    message = e.message ?: "Failed to archive course"
                )
            }
        }
    }

    /**
     * Search courses
     */
    fun searchCourses(query: String) {
        viewModelScope.launch {
            try {
                courseRepository.searchCourses(query).collect { courses ->
                    val currentState = _uiState.value
                    if (currentState is CourseUiState.Success) {
                        _uiState.value = currentState.copy(courses = courses)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(
                    message = e.message ?: "Failed to search courses"
                )
            }
        }
    }

    /**
     * Refresh data
     */
    fun refresh() {
        loadCourses()
    }
}

/**
 * CourseUiState - UI state for courses list
 */
sealed class CourseUiState {
    object Loading : CourseUiState()

    data class Success(
        val courses: List<Course>,
        val totalCourses: Int,
        val totalCredits: Int,
        val currentSemester: String
    ) : CourseUiState()

    data class Error(
        val message: String
    ) : CourseUiState()
}

/**
 * CourseDetailUiState - UI state for course detail
 */
sealed class CourseDetailUiState {
    object Loading : CourseDetailUiState()

    data class Success(
        val courseWithSchedules: CourseWithSchedules,
        val pendingTaskCount: Int
    ) : CourseDetailUiState()

    data class Error(
        val message: String
    ) : CourseDetailUiState()
}
