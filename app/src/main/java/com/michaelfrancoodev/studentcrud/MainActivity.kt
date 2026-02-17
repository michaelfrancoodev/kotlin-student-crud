package com.michaelfrancoodev.studentcrud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.michaelfrancoodev.studentcrud.data.local.database.AppDatabase
import com.michaelfrancoodev.studentcrud.data.repository.CourseRepository
import com.michaelfrancoodev.studentcrud.data.repository.ScheduleRepository
import com.michaelfrancoodev.studentcrud.data.repository.StudentRepository
import com.michaelfrancoodev.studentcrud.data.repository.TaskRepository
import com.michaelfrancoodev.studentcrud.ui.navigation.BottomNavBar
import com.michaelfrancoodev.studentcrud.ui.navigation.NavGraph
import com.michaelfrancoodev.studentcrud.ui.navigation.Screen
import com.michaelfrancoodev.studentcrud.ui.screens.courses.CourseViewModel
import com.michaelfrancoodev.studentcrud.ui.screens.home.HomeViewModel
import com.michaelfrancoodev.studentcrud.ui.theme.SomaSmartTheme

/**
 * MainActivity - Entry point for Soma Smart app
 * Sets up navigation, theme, and dependency injection
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        enableEdgeToEdge()
        
        // Initialize database and repositories
        val database = AppDatabase.getInstance(applicationContext)
        val studentRepository = StudentRepository(database.studentDao())
        val courseRepository = CourseRepository(database.courseDao())
        val scheduleRepository = ScheduleRepository(database.classScheduleDao())
        val taskRepository = TaskRepository(database.taskDao())
        
        setContent {
            SomaSmartTheme {
                SomaSmartApp(
                    studentRepository = studentRepository,
                    courseRepository = courseRepository,
                    scheduleRepository = scheduleRepository,
                    taskRepository = taskRepository
                )
            }
        }
    }
}

/**
 * SomaSmartApp - Main composable for app navigation
 */
@Composable
fun SomaSmartApp(
    studentRepository: StudentRepository,
    courseRepository: CourseRepository,
    scheduleRepository: ScheduleRepository,
    taskRepository: TaskRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Check if current route should show bottom bar
    val showBottomBar = when (currentRoute) {
        Screen.Home.route,
        Screen.Courses.route,
        Screen.Tasks.route,
        Screen.Timetable.route,
        Screen.Profile.route -> true
        else -> false
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            studentRepository = studentRepository,
            courseRepository = courseRepository,
            scheduleRepository = scheduleRepository,
            taskRepository = taskRepository
        )
    }
}
