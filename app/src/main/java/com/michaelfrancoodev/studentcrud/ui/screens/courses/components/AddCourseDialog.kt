package com.michaelfrancoodev.studentcrud.ui.screens.courses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import com.michaelfrancoodev.studentcrud.data.local.entity.Course
import java.time.DayOfWeek

/**
 * AddCourseDialog - Dialog for adding a new course
 * Allows entering course details and (optionally) schedules
 */
@Composable
fun AddCourseDialog(
    onDismiss: () -> Unit,
    onSave: (course: Course, schedules: List<ClassSchedule>) -> Unit,
    modifier: Modifier = Modifier
) {
    var courseName by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var credits by remember { mutableStateOf("3") }
    var semester by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Course") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Course Name
                OutlinedTextField(
                    value = courseName,
                    onValueChange = { 
                        courseName = it
                        showError = false
                    },
                    label = { Text("Course Name *") },
                    placeholder = { Text("e.g., Data Structures") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && courseName.isBlank(),
                    singleLine = true
                )

                // Course Code
                OutlinedTextField(
                    value = courseCode,
                    onValueChange = { 
                        courseCode = it.uppercase()
                        showError = false
                    },
                    label = { Text("Course Code *") },
                    placeholder = { Text("e.g., CS201") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && courseCode.isBlank(),
                    singleLine = true
                )

                // Credits
                OutlinedTextField(
                    value = credits,
                    onValueChange = { 
                        if (it.isEmpty() || it.toIntOrNull() != null) {
                            credits = it
                        }
                    },
                    label = { Text("Credits") },
                    placeholder = { Text("3") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // Semester
                OutlinedTextField(
                    value = semester,
                    onValueChange = { semester = it },
                    label = { Text("Semester") },
                    placeholder = { Text("e.g., Fall 2026") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Instructor
                OutlinedTextField(
                    value = instructor,
                    onValueChange = { instructor = it },
                    label = { Text("Instructor") },
                    placeholder = { Text("e.g., Dr. Smith") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (showError) {
                    Text(
                        text = "* Required fields",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You can add schedules later from course details",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (courseName.isBlank() || courseCode.isBlank()) {
                        showError = true
                        return@Button
                    }

                    val course = Course(
                        id = 0, // Will be auto-generated
                        name = courseName.trim(),
                        code = courseCode.trim(),
                        instructor = instructor.trim(),
                        credits = credits.toIntOrNull() ?: 3,
                        semester = semester.trim(),
                        color = generateRandomColor(),
                        room = "",
                        description = ""
                    )

                    onSave(course, emptyList()) // No schedules for now
                }
            ) {
                Text("Add Course")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}

/**
 * Generate a random color for the course
 */
private fun generateRandomColor(): String {
    val colors = listOf(
        "#6750A4", // Purple
        "#D32F2F", // Red
        "#1976D2", // Blue
        "#388E3C", // Green
        "#F57C00", // Orange
        "#C2185B", // Pink
        "#0097A7", // Cyan
        "#7B1FA2", // Deep Purple
        "#5D4037", // Brown
        "#455A64"  // Blue Grey
    )
    return colors.random()
}
