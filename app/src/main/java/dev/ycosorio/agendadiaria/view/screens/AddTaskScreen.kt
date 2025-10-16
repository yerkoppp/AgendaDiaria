package dev.ycosorio.agendadiaria.view.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.ycosorio.agendadiaria.view.components.CustomDatePickerDialog
import dev.ycosorio.agendadiaria.view.components.CustomTimePickerDialog
import dev.ycosorio.agendadiaria.viewmodel.TaskListUiState
import dev.ycosorio.agendadiaria.viewmodel.TaskListViewModel

/**
 * Pantalla para crear una nueva tarea.
 *
 * Proporciona campos de texto para el nombre, descripción, fecha y hora de la tarea.
 * Utiliza un [Scaffold] con una [TopAppBar] para navegación, una [BottomAppBar] para acciones secundarias
 * y un [FloatingActionButton] para la acción principal de guardar.
 *
 * @param taskListViewModel La instancia del ViewModel que gestiona el estado.
 * @param uiState El estado actual de la UI, observado desde el ViewModel.
 * @param onNavigateBack Lambda para manejar el evento de retroceso.
 * @param navigateToTaskList Lambda para navegar a la pantalla de la lista de tareas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskListViewModel: TaskListViewModel,
    uiState: TaskListUiState,
    onNavigateBack: () -> Unit,
    navigateToTaskList: () -> Unit = {},
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    if (showDatePickerDialog) {
        CustomDatePickerDialog(
            onDateSelected = { newDate -> taskListViewModel.updateNewTaskDate(newDate) },
            onDismiss = { showDatePickerDialog = false }
        )
    }

    if (showTimePickerDialog) {
        CustomTimePickerDialog(
            onTimeSelected = { newTime -> taskListViewModel.updateNewTaskTime(newTime) },
            onDismiss = { showTimePickerDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Nueva Actividad") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    taskListViewModel.saveTask()
                    navigateToTaskList() // Navega a la lista después de guardar
                },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Save, "Guardar Tarea", tint = MaterialTheme.colorScheme.onSecondary)
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Button(
                    onClick = navigateToTaskList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(
                        text = "Ver lista de actividades",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Permite scroll
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CAMPO DE NOMBRE ---
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.newTaskName,
                onValueChange = { taskListViewModel.updateNewTaskName(it) },
                label = { Text("Nombre de la actividad") },
                singleLine = true
            )

            // --- CAMPOS DE FECHA Y HORA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val interactionSourceDate = remember { MutableInteractionSource() }
                if (interactionSourceDate.collectIsPressedAsState().value) {
                    showDatePickerDialog = true
                }
                OutlinedTextField(
                    modifier = Modifier.weight(1f), readOnly = true, value = uiState.newTaskDate,
                    onValueChange = {}, label = { Text("Fecha") },
                    trailingIcon = { Icon(Icons.Default.DateRange, "Seleccionar Fecha") },
                    interactionSource = interactionSourceDate
                )

                val interactionSourceTime = remember { MutableInteractionSource() }
                if (interactionSourceTime.collectIsPressedAsState().value) {
                    showTimePickerDialog = true
                }
                OutlinedTextField(
                    modifier = Modifier.weight(1f), readOnly = true, value = uiState.newTaskTime,
                    onValueChange = {}, label = { Text("Hora") },
                    trailingIcon = { Icon(Icons.Default.MoreTime, "Seleccionar Hora") },
                    interactionSource = interactionSourceTime
                )
            }

            // --- CAMPO DE DESCRIPCIÓN ---
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Aumentamos un poco la altura
                value = uiState.newTaskDescription,
                onValueChange = { taskListViewModel.updateNewTaskDescription(it) },
                label = { Text("Descripción (opcional)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}