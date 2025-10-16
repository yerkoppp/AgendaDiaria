package dev.ycosorio.agendadiaria.viewmodel

import android.service.autofill.CustomDescription
import androidx.lifecycle.ViewModel
import dev.ycosorio.agendadiaria.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskListViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState

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
            val name = currentState.newTaskName.trim()
            val isCompleted = currentState.newTaskIsCompleted
            val description = currentState.newTaskDescription
            val date = currentState.newTaskDate
            val time = currentState.newTaskTime

            if(name.isNotBlank()){
                val newTask = Task (
                    name = name,
                    isCompleted = isCompleted,
                    date = date,
                    time = time,
                    description = description
                    )

                val updatedTaskList = listOf(newTask) + currentState.taskList

                currentState.copy(
                    taskList = updatedTaskList,
                    newTaskName = "",
                    newTaskIsCompleted = false,
                    newTaskDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    newTaskTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                    newTaskDescription = ""
                )
            } else {
                currentState
            }

        }
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
    val newTaskName: String = "",
    val newTaskIsCompleted: Boolean = false,
    val newTaskDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    val newTaskTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
    val newTaskDescription: String = ""
)