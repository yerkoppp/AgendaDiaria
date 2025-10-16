package dev.ycosorio.agendadiaria.data

import java.util.UUID

data class Task (
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isCompleted: Boolean = false,
    val date: String,
    val time: String,
    val description: String
)