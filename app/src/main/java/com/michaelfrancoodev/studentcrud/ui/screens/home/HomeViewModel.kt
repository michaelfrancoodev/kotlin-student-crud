package com.michaelfrancoodev.studentcrud.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelfrancoodev.studentcrud.data.local.entity.Student
import com.michaelfrancoodev.studentcrud.data.repository.CourseRepository
import com.michaelfrancoodev.studentcrud.data.repository.ScheduleRepository
import com.michaelfrancoodev.studentcrud.data.repository.StudentRepository
import com.michaelfrancoodev.studentcrud.data.repository.TaskRepository
import com.michaelfrancoodev.studentcrud.domain.model.ClassScheduleWithCourse
import com.michaelfrancoodev.studentcrud.domain.model.DashboardStats
import com.michaelfrancoodev.studentcrud.domain.model.TaskWithCourse
import com.michaelfrancoodev.studentcrud.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * HomeViewModel - Manages home screen state and business logic
 */
class HomeViewModel(
    private val studentRepository: StudentRepository,
    private val courseRepository: CourseRepository,
    private val scheduleRepository: ScheduleRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    /**
     * Load all dashboard data
     */
    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                _uiState.value = HomeUiState.Loading

                // Combine multiple flows
                combine(
                    studentRepository.getProfile(),
                    courseRepository.getActiveCourses(),
                    taskRepository.getPendingTasks(),
                    taskRepository.getOverdueTasks()
                ) { student, courses, pendingTasks, overdueTasks ->

                    // Get current semester from student profile
                    val currentSemester = student?.currentSemester ?: "Semester 1"

                    // Filter courses by current semester
                    val semesterCourses = courses.filter { it.semester == currentSemester }

                    // Get today's and tomorrow's schedules
                    val todayDayName = DateUtils.getCurrentDayName()
                    val tomorrowDayName = DateUtils.getTomorrowDayName()

                    val todaySchedules = scheduleRepository.getTodaysSchedules(todayDayName)
                    val tomorrowSchedules = scheduleRepository.getTomorrowSchedules(tomorrowDayName)

                    // Map schedules to ClassScheduleWithCourse
                    val todayClasses = todaySchedules.mapNotNull { schedule ->
                        val course = semesterCourses.find { it.id == schedule.courseId }
                        course?.let {
                            ClassScheduleWithCourse(
                                schedule = schedule,
                                courseName = it.courseName,
                                courseCode = it.courseCode,
                                courseColor = it.color,
                                instructor = it.instructor,
                                credits = it.credits
                            )
                        }
                    }

                    val tomorrowClasses = tomorrowSchedules.mapNotNull { schedule ->
                        val course = semesterCourses.find { it.id == schedule.courseId }
                        course?.let {
                            ClassScheduleWithCourse(
                                schedule = schedule,
                                courseName = it.courseName,
                                courseCode = it.courseCode,
                                courseColor = it.color,
                                instructor = it.instructor,
                                credits = it.credits
                            )
                        }
                    }

                    // Map tasks to TaskWithCourse
                    val tasksWithCourses = pendingTasks.take(5).mapNotNull { task ->
                        val course = courses.find { it.id == task.courseId }
                        course?.let {
                            TaskWithCourse(
                                task = task,
                                courseName = it.courseName,
                                courseCode = it.courseCode,
                                courseColor = it.color,
                                credits = it.credits
                            )
                        }
                    }

                    // Calculate statistics
                    val totalCredits = courseRepository.getTotalCreditsBySemester(currentSemester)
                    val pendingTaskCount = taskRepository.getPendingTaskCount()
                    val overdueTaskCount = taskRepository.getOverdueTaskCount()

                    // Get tasks due today
                    val startOfDay = DateUtils.getStartOfDay()
                    val endOfDay = DateUtils.getEndOfDay()
                    val dueTodayCount = pendingTasks.count {
                        it.dueDate in startOfDay..endOfDay
                    }

                    // Build dashboard stats
                    val stats = DashboardStats(
                        activeCourseCount = semesterCourses.size,
                        pendingTaskCount = pendingTaskCount,
                        todayStudyHours = 0, // TODO: Implement study tracking
                        totalCredits = totalCredits,
                        totalClassHours = totalCredits, // Approximation
                        currentStreak = student?.currentStreak ?: 0,
                        weeklyStudyGoal = student?.dailyStudyGoalHours?.times(7) ?: 20,
                        weeklyStudyProgress = 0, // TODO: Implement study tracking
                        todayClasses = todayClasses.sortedBy { it.schedule.startTime },
                        tomorrowClasses = tomorrowClasses.sortedBy { it.schedule.startTime },
                        overdueTaskCount = overdueTaskCount,
                        dueTodayTaskCount = dueTodayCount,
                        pendingTasks = tasksWithCourses.sortedBy { it.task.dueDate }
                    )

                    HomeUiState.Success(
                        student = student,
                        stats = stats
                    )
                }.collect { state ->
                    _uiState.value = state
                }

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(
                    message = e.message ?: "Failed to load dashboard data"
                )
            }
        }
    }

    /**
     * Refresh dashboard data
     */
    fun refresh() {
        loadDashboardData()
    }
}

/**
 * HomeUiState - UI state for home screen
 */
sealed class HomeUiState {
    object Loading : HomeUiState()

    data class Success(
        val student: Student?,
        val stats: DashboardStats
    ) : HomeUiState()

    data class Error(
        val message: String
    ) : HomeUiState()
}
