package dev.ycosorio.agendadiaria.navigation

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
object Home

@Serializable
object TaskList

@Serializable
object AddTask

@Serializable
data class Details(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isCompleted: Boolean = false,
    val date: String,
    val time: String,
    val description: String
)