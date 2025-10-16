package dev.ycosorio.agendadiaria.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ycosorio.agendadiaria.data.Task

/**
 * Un Composable que muestra un único elemento de tarea en una tarjeta elevada.
 *
 * Proporciona interacciones para marcar la tarea como completada, cambiar la fecha y la hora,
 * y navegar a una pantalla de detalles. El estilo visual cambia si la tarea está completada.
 *
 * @param modifier El [Modifier] que se aplicará a la tarjeta.
 * @param task El objeto [Task] que se va a mostrar.
 * @param onToggleCompletion Lambda que se invoca cuando el interruptor de completado cambia de estado.
 * @param onDateChange Lambda que se invoca cuando se selecciona una nueva fecha desde el diálogo.
 * @param onTimeChange Lambda que se invoca cuando se selecciona una nueva hora desde el diálogo.
 * @param onClick Lambda que se invoca cuando se hace clic en la tarjeta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onToggleCompletion: (Task) -> Unit,
    onDateChange: (Task, String) -> Unit,
    onTimeChange: (Task, String) -> Unit,
    onClick: () -> Unit = {}
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    val cardColor by animateColorAsState(
        targetValue = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300),
        label = "CardColorAnimation"
    )

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = task.isCompleted,
                onCheckedChange = { onToggleCompletion(task) },
                modifier = Modifier.padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fila para Fecha y Hora
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Botón de Fecha
                    TextButton(onClick = { showDatePickerDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Fecha",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = task.date,
                            modifier = Modifier.padding(start = 4.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                    // Botón de Hora
                    TextButton(onClick = { showTimePickerDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreTime,
                            contentDescription = "Hora",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = task.time,
                            modifier = Modifier.padding(start = 4.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    if (showDatePickerDialog) {
        CustomDatePickerDialog(
            onDateSelected = { newDate -> onDateChange(task, newDate) },
            onDismiss = { showDatePickerDialog = false }
        )
    }
    if (showTimePickerDialog) {
        CustomTimePickerDialog(
            onTimeSelected = { newTime -> onTimeChange(task, newTime) },
            onDismiss = { showTimePickerDialog = false }
        )
    }
}