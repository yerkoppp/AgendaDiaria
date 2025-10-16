package dev.ycosorio.agendadiaria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ycosorio.agendadiaria.data.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskListViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState

    // Canal para enviar mensajes de confirmación (Snackbar) ---
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    // Bandera para controlar que la carga inicial se haga solo una vez ---
    private var isInitialDataLoaded = false

    /**
     * Simula la carga de tareas iniciales. Gracias a la bandera 'isInitialDataLoaded',
     * este código solo se ejecutará una vez durante el ciclo de vida del ViewModel.
     */
    fun loadInitialTasks() {
        if (isInitialDataLoaded) return // Si ya se cargó, no hacer nada

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(2000) // La espera de 2 segundos
            val sampleTasks = listOf(
                Task(name = "Pasear al perro", description = "Dar una vuelta a la manzana.", date = "15/10/2025", time = "08:00", isCompleted = true),
                Task(name = "Reunión de proyecto", description = "Revisar avances del sprint.", date = "15/10/2025", time = "10:30")
            )
            _uiState.update { it.copy(taskList = sampleTasks, isLoading = false) }
            isInitialDataLoaded = true // Marcamos que la carga inicial ya se completó
        }
    }

    fun updateNewTaskName(newName: String, ){
        _uiState.update { currentState ->
            currentState.copy(newTaskName = newName)
        }
    }

    fun updateNewTaskDescription(newDescription: String, ){
        _uiState.update { currentState ->
            currentState.copy(newTaskDescription = newDescription)
        }
    }

    fun updateNewTaskDate(newDate: String){
        _uiState.update { currentState ->
            currentState.copy(newTaskDate = newDate)
        }
    }

    fun updateNewTaskTime(newTime: String){
        _uiState.update { currentState ->
            currentState.copy(newTaskTime = newTime)
        }
    }

    fun updateNewTaskDate(taskToUpdate: Task, newDate: String){
        _uiState.update { currentState ->
            val updatedTaskList = currentState.taskList.map{ existingTask ->
                if(existingTask.id == taskToUpdate.id){
                    existingTask.copy(date = newDate)
                } else existingTask
            }
            currentState.copy(taskList = updatedTaskList)
        }
    }

    fun updateNewTaskTime(taskToUpdate: Task, newTime: String){
        _uiState.update { currentState ->
            val updatedTaskList = currentState.taskList.map{ existingTask ->
                if(existingTask.id == taskToUpdate.id){
                    existingTask.copy(time = newTime)
                } else {
                    existingTask
                }
            }
            currentState.copy(taskList = updatedTaskList)
        }
    }

    fun saveTask(){
        _uiState.update { currentState ->
            // Usamos la scope function 'let' para ejecutar el bloque solo si el nombre no está en blanco.
            currentState.newTaskName.trim().takeIf { it.isNotBlank() }?.let { name ->
                val newTask = Task (
                    name = name,
                    isCompleted = currentState.newTaskIsCompleted,
                    date = currentState.newTaskDate,
                    time = currentState.newTaskTime,
                    description = currentState.newTaskDescription
                )

                val updatedTaskList = listOf(newTask) + currentState.taskList

                return@update currentState.copy(
                    taskList = updatedTaskList,
                    snackbarMessage = "¡Tarea '${name}' guardada!", // <-- CAMBIO: Se establece el mensaje
                    newTaskName = "",
                    newTaskDescription = "",
                    newTaskIsCompleted = false,
                    newTaskDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    newTaskTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
            currentState
        }
    }

    /**
     * Avisa al ViewModel que el mensaje del Snackbar ya fue mostrado.
     */
    fun snackbarMessageShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun removeTask(task: Task){
        _uiState.update { currentState ->
            val updatedTaskList = currentState.taskList.filter { it.id != task.id }
            currentState.copy(taskList = updatedTaskList)

        }
    }

    fun toggleTaskCompletion(task: Task){
        _uiState.update { currentState ->
            val updatedTaskList = currentState.taskList.map {existingTask ->
                if (existingTask.id == task.id){
                    existingTask.copy(isCompleted = !existingTask.isCompleted)
                } else {
                    existingTask
                }
            }
            currentState.copy(taskList = updatedTaskList)
        }
    }

}
data class TaskListUiState(
    val taskList: List<Task> = emptyList(),
    val snackbarMessage: String? = null,
    val isLoading: Boolean = false,
    val newTaskName: String = "",
    val newTaskIsCompleted: Boolean = false,
    val newTaskDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    val newTaskTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
    val newTaskDescription: String = ""
)