package dev.ycosorio.agendadiaria.data

import dev.ycosorio.agendadiaria.navigation.Details

/**
 * Convierte un objeto [Task] en un objeto [Details] para la navegación segura.
 * Esta es una función de extensión, lo que permite llamarla directamente desde una instancia de Task.
 * Ejemplo: `myTask.toDetails()`
 */
fun Task.toDetails(): Details {
    return Details(
        id = this.id,
        name = this.name,
        isCompleted = this.isCompleted,
        date = this.date,
        time = this.time,
        description = this.description
    )
}