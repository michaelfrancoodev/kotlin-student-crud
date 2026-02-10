package com.michaelfrancoodev.studentcrud.ui.navigation

/**
 * Screen - Sealed class representing all app destinations
 * Used for type-safe navigation in Soma Smart
 */
sealed class Screen(val route: String) {

    // Main bottom navigation screens
    object Home : Screen("home")
    object Courses : Screen("courses")
    object Timetable : Screen("timetable")
    object Tasks : Screen("tasks")
    object Profile : Screen("profile")

    // Detail screens
    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: Int) = "course_detail/$courseId"
    }

    object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }

    // Dialog/Modal screens
    object AddCourse : Screen("add_course")
    object EditCourse : Screen("edit_course/{courseId}") {
        fun createRoute(courseId: Int) = "edit_course/$courseId"
    }

    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }

    object EditProfile : Screen("edit_profile")

    // Settings screens
    object Settings : Screen("settings")
    object About : Screen("about")

    companion object {
        // List of bottom navigation screens
        val bottomNavScreens = listOf(
            Home,
            Courses,
            Timetable,
            Tasks,
            Profile
        )

        // Get screen by route
        fun fromRoute(route: String?): Screen? {
            return when {
                route == null -> null
                route.startsWith("home") -> Home
                route.startsWith("courses") -> Courses
                route.startsWith("timetable") -> Timetable
                route.startsWith("tasks") -> Tasks
                route.startsWith("profile") -> Profile
                route.startsWith("course_detail") -> CourseDetail
                route.startsWith("task_detail") -> TaskDetail
                route.startsWith("add_course") -> AddCourse
                route.startsWith("edit_course") -> EditCourse
                route.startsWith("add_task") -> AddTask
                route.startsWith("edit_task") -> EditTask
                route.startsWith("edit_profile") -> EditProfile
                route.startsWith("settings") -> Settings
                route.startsWith("about") -> About
                else -> null
            }
        }
    }
}
