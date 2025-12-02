package com.juno.colorteachingaids.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juno.colorteachingaids.ui.launch.LaunchScreen
import com.juno.colorteachingaids.ui.onboarding.OnboardingScreen
import com.juno.colorteachingaids.ui.student.* // ktlint-disable no-wildcard-imports
import com.juno.colorteachingaids.ui.teacher.*

object Routes {
    const val LAUNCH = "launch"
    const val NEW_TEACHER_PROFILE = "new_teacher_profile"
    const val EDIT_TEACHER_PROFILE = "edit_teacher_profile"
    const val ONBOARDING = "onboarding"
    const val ADD_STUDENT = "add_student"
    const val EDIT_STUDENT = "edit_student"
    const val EXISTING_TEACHER = "existing_teacher"
    const val TEACHER_DASHBOARD = "teacher_dashboard"
    const val TEACHER_STUDENT_DETAIL = "teacher_student_detail"
    const val LEARNING_PATH = "learning_path"
    const val TEACHER_NOTES = "teacher_notes"
    const val ADD_TEACHER_NOTE = "add_teacher_note"
    const val STUDENT_DASHBOARD = "student_dashboard"
    const val ACTIVITY_LAUNCHER = "activity_launcher"
    const val ACTIVITY_SETTINGS = "activity_settings"
    const val MODULE_1 = "module_1"
    const val MODULE_2 = "module_2"
    const val MODULE_3 = "module_3"
    const val MODULE_4 = "module_4"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LAUNCH) {
        composable(Routes.LAUNCH) {
            LaunchScreen(
                onNavigateToNewTeacher = { navController.navigate(Routes.NEW_TEACHER_PROFILE) },
                onNavigateToExistingTeacher = { navController.navigate(Routes.EXISTING_TEACHER) }
            )
        }
        composable(Routes.NEW_TEACHER_PROFILE) {
            NewTeacherProfileScreen(
                onProfileCreated = { teacherId ->
                    navController.navigate("${Routes.ONBOARDING}/$teacherId") { popUpTo(Routes.LAUNCH) }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Routes.EDIT_TEACHER_PROFILE}/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            NewTeacherProfileScreen(
                onProfileCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Routes.ONBOARDING}/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            val teacherId = it.arguments?.getInt("teacherId")!!
            OnboardingScreen(
                onNavigateToAddStudent = {
                    navController.navigate("${Routes.ADD_STUDENT}/$teacherId")
                },
                onNavigateToTeacherDashboard = {
                    navController.navigate("${Routes.TEACHER_DASHBOARD}/$teacherId")
                }
            )
        }
        composable(
            route = "${Routes.ADD_STUDENT}/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            AddStudentScreen(
                onBack = { navController.popBackStack() },
                onStudentAdded = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Routes.EDIT_STUDENT}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            AddStudentScreen(
                onBack = { navController.popBackStack() },
                onStudentAdded = { navController.popBackStack() } 
            )
        }
        composable(Routes.EXISTING_TEACHER) {
            ExistingTeacherScreen(
                onBack = { navController.popBackStack() },
                onTeacherSelected = { teacherId ->
                    navController.navigate("${Routes.TEACHER_DASHBOARD}/$teacherId")
                },
                onNavigateToEditTeacher = { teacherId ->
                    navController.navigate("${Routes.EDIT_TEACHER_PROFILE}/$teacherId")
                }
            )
        }
        composable(
            route = "${Routes.TEACHER_DASHBOARD}/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            val teacherId = it.arguments?.getInt("teacherId")!!
            TeacherDashboardScreen(
                onNavigateToStudentDashboard = { studentId ->
                    navController.navigate("${Routes.TEACHER_STUDENT_DETAIL}/$studentId")
                },
                onNavigateToAddStudent = {
                    navController.navigate("${Routes.ADD_STUDENT}/$teacherId")
                },
                onNavigateToEditStudent = { studentId ->
                    navController.navigate("${Routes.EDIT_STUDENT}/$studentId")
                }
            )
        }
        composable(
            route = "${Routes.TEACHER_STUDENT_DETAIL}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            TeacherStudentDetailScreen(
                onBack = { navController.popBackStack() },
                onLaunchActivity = { studentId ->
                    navController.navigate("${Routes.ACTIVITY_LAUNCHER}/$studentId")
                },
                onEditStudent = { studentId ->
                    navController.navigate("${Routes.EDIT_STUDENT}/$studentId")
                },
                onNavigateToLearningPath = { studentId ->
                    navController.navigate("${Routes.LEARNING_PATH}/$studentId")
                },
                onNavigateToTeacherNotes = { studentId ->
                    navController.navigate("${Routes.TEACHER_NOTES}/$studentId")
                }
            )
        }
        composable(
            route = "${Routes.LEARNING_PATH}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            LearningPathScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "${Routes.TEACHER_NOTES}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            TeacherNotesScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAddNote = { studentId ->
                    navController.navigate("${Routes.ADD_TEACHER_NOTE}/$studentId")
                }
            )
        }
        composable(
            route = "${Routes.ADD_TEACHER_NOTE}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            AddNoteScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "${Routes.STUDENT_DASHBOARD}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            StudentDashboardScreen(onNavigateToActivityLauncher = { studentId ->
                navController.navigate("${Routes.ACTIVITY_LAUNCHER}/$studentId")
            })
        }
        composable(
            route = "${Routes.ACTIVITY_LAUNCHER}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            val studentId = it.arguments?.getInt("studentId") !!
            ActivityLauncherScreen(
                onNavigateToModule1 = { navController.navigate("${Routes.MODULE_1}/$studentId") },
                onNavigateToModule2 = { navController.navigate("${Routes.MODULE_2}/$studentId") },
                onNavigateToModule3 = { navController.navigate("${Routes.MODULE_3}/$studentId") },
                onNavigateToModule4 = { navController.navigate("${Routes.MODULE_4}/$studentId") },
                onNavigateToSettings = { id, module ->
                    navController.navigate("${Routes.ACTIVITY_SETTINGS}/$id/$module")
                },
                onBack = { navController.popBackStack() },
                onExit = { navController.navigate(Routes.LAUNCH) { popUpTo(0) } }
            )
        }
        dialog(
            route = "${Routes.ACTIVITY_SETTINGS}/{studentId}/{moduleId}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("moduleId") { type = NavType.StringType }
            )
        ) {
            ActivitySettingsDialog(onDismiss = { navController.popBackStack() })
        }
        composable(
            route = "${Routes.MODULE_1}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            Module1Screen(onDone = { navController.popBackStack() })
        }
        composable(
            route = "${Routes.MODULE_2}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            Module2Screen(onDone = { navController.popBackStack() })
        }
        composable(
            route = "${Routes.MODULE_3}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            Module3Screen(onDone = { navController.popBackStack() })
        }
        composable(
            route = "${Routes.MODULE_4}/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            Module4Screen(onBack = { navController.popBackStack() })
        }
    }
}
