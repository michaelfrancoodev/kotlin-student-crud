package com.michaelfrancoodev.studentcrud.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.michaelfrancoodev.studentcrud.ui.screens.courses.CourseDetailScreen
import com.michaelfrancoodev.studentcrud.ui.screens.courses.CoursesScreen
import com.michaelfrancoodev.studentcrud.ui.screens.home.HomeScreen
import com.michaelfrancoodev.studentcrud.ui.screens.profile.ProfileScreen
import com.michaelfrancoodev.studentcrud.ui.screens.tasks.TasksScreen
import com.michaelfrancoodev.studentcrud.ui.screens.timetable.TimetableScreen

/**
 * NavGraph - Main navigation graph for Soma Smart
 * Defines all navigation routes and screen compositions
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ========================================
        // MAIN BOTTOM NAVIGATION SCREENS
        // ========================================

        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCourse = { courseId ->
                    navController.navigate(Screen.CourseDetail.createRoute(courseId))
                },
                onNavigateToTask = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onNavigateToTimetable = {
                    navController.navigate(Screen.Timetable.route)
                }
            )
        }

        // Courses Screen
        composable(route = Screen.Courses.route) {
            CoursesScreen(
                onNavigateToCourseDetail = { courseId ->
                    navController.navigate(Screen.CourseDetail.createRoute(courseId))
                },
                onNavigateToAddCourse = {
                    navController.navigate(Screen.AddCourse.route)
                }
            )
        }

        // Timetable Screen
        composable(route = Screen.Timetable.route) {
            TimetableScreen(
                onNavigateToCourse = { courseId ->
                    navController.navigate(Screen.CourseDetail.createRoute(courseId))
                }
            )
        }

        // Tasks Screen
        composable(route = Screen.Tasks.route) {
            TasksScreen(
                onNavigateToTaskDetail = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddTask.route)
                }
            )
        }

        // Profile Screen
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                }
            )
        }

        // ========================================
        // DETAIL SCREENS
        // ========================================

        // Course Detail Screen
        composable(
            route = Screen.CourseDetail.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            CourseDetailScreen(
                courseId = courseId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = {
                    navController.navigate(Screen.EditCourse.createRoute(courseId))
                }
            )
        }

        // Task Detail Screen (placeholder - will be implemented)
        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            // TaskDetailScreen will be implemented later
            // For now, just pop back
            navController.popBackStack()
        }

        // ========================================
        // ADD/EDIT SCREENS (Dialogs handled in screens)
        // ========================================

        // Add Course (handled as dialog in CoursesScreen)
        composable(route = Screen.AddCourse.route) {
            navController.popBackStack()
        }

        // Edit Course (handled as dialog in CourseDetailScreen)
        composable(
            route = Screen.EditCourse.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType }
            )
        ) {
            navController.popBackStack()
        }

        // Add Task (handled as dialog in TasksScreen)
        composable(route = Screen.AddTask.route) {
            navController.popBackStack()
        }

        // Edit Task (handled as dialog in TaskDetailScreen)
        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) {
            navController.popBackStack()
        }

        // Edit Profile (handled as dialog in ProfileScreen)
        composable(route = Screen.EditProfile.route) {
            navController.popBackStack()
        }

        // ========================================
        // SETTINGS SCREENS (Placeholders)
        // ========================================

        composable(route = Screen.Settings.route) {
            // Settings screen will be implemented later
            navController.popBackStack()
        }

        composable(route = Screen.About.route) {
            // About screen will be implemented later
            navController.popBackStack()
        }
    }
}
