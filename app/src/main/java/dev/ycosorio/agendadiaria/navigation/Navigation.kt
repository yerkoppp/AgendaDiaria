package dev.ycosorio.agendadiaria.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.ycosorio.agendadiaria.view.screens.AddTaskScreen
import dev.ycosorio.agendadiaria.view.screens.DetailsScreen
import dev.ycosorio.agendadiaria.view.screens.HomeScreen
import dev.ycosorio.agendadiaria.view.screens.TaskListScreen
import dev.ycosorio.agendadiaria.viewmodel.TaskListViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val taskListViewModel: TaskListViewModel = viewModel()

    NavHost(navController = navController, startDestination = Home){
        composable<Home>{
            HomeScreen(
                navigateToAddTask = { navController.navigate(AddTask) }
            )
        }
        composable<AddTask>{

            val uiState by taskListViewModel.uiState.collectAsStateWithLifecycle()

            AddTaskScreen(
                navigateToTaskList = { navController.navigate(TaskList) },
                taskListViewModel = taskListViewModel,
                uiState = uiState,
                onNavigateBack = { navController.navigateUp() }

            )
        }
        composable<TaskList>{
            TaskListScreen(
                taskListViewModel = taskListViewModel,
                onNavigateBack = { navController.navigateUp() },
                navigateToDetails = { task ->
                    navController.navigate(Details(
                            id = task.id,
                            name = task.name,
                            isCompleted = task.isCompleted,
                            date = task.date,
                            time = task.time,
                            description = task.description,
                        )
                    )
                }
            )
        }
        composable<Details>{ backStackEntry->
            var details = backStackEntry.toRoute<Details>()

            DetailsScreen(
                name = details.name,
                isCompleted = details.isCompleted,
                date = details.date,
                time = details.time,
                description = details.description,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
