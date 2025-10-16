package dev.ycosorio.agendadiaria.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ycosorio.agendadiaria.data.Task
import dev.ycosorio.agendadiaria.view.components.SwipeToDeleteContainer
import dev.ycosorio.agendadiaria.view.components.TaskItem
import dev.ycosorio.agendadiaria.viewmodel.TaskListViewModel

/**
 * Muestra la lista de tareas del usuario.
 *
 * Esta pantalla utiliza un [Scaffold] con una [TopAppBar] para la navegación y un [LazyColumn]
 * para mostrar eficientemente la lista de tareas. También maneja un estado vacío para cuando
 * no hay tareas que mostrar.
 *
 * @param modifier El [Modifier] que se aplicará al contenedor raíz.
 * @param taskListViewModel La instancia del ViewModel que contiene el estado y la lógica de la pantalla.
 * @param onNavigateBack Lambda para manejar el evento de retroceso en la navegación.
 * @param navigateToDetails Lambda para navegar a la pantalla de detalles de una tarea específica.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    taskListViewModel: TaskListViewModel,
    onNavigateBack: () -> Unit,
    navigateToDetails: (Task) -> Unit = {}
) {
    val uiState by taskListViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Actividades") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.taskList.isEmpty()) {
                // Estado vacío cuando no hay tareas
                Text(
                    text = "No tienes actividades pendientes.\n¡Añade una nueva!",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                // Lista de tareas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = uiState.taskList,
                        key = { task -> task.id }
                    ) { task ->
                        SwipeToDeleteContainer(
                            item = task,
                            onDelete = { taskToDelete -> taskListViewModel.removeTask(taskToDelete) }
                        ) {
                            TaskItem(
                                task = task,
                                onToggleCompletion = { taskToToggle ->
                                    taskListViewModel.toggleTaskCompletion(taskToToggle)
                                },
                                onDateChange = { taskToUpdate, newDate ->
                                    taskListViewModel.updateNewTaskDate(taskToUpdate, newDate)
                                },
                                onTimeChange = { taskToUpdate, newTime ->
                                    taskListViewModel.updateNewTaskTime(taskToUpdate, newTime)
                                },
                                onClick = { navigateToDetails(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}