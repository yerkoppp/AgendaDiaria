package dev.ycosorio.agendadiaria.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePickerDialog(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState()

    TimePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    // 1. Obtiene la hora y el minuto del estado
                    val selectedHour = timePickerState.hour
                    val selectedMinute = timePickerState.minute

                    // 2. Crea un objeto LocalTime con esos valores
                    val localTime = LocalTime.of(selectedHour, selectedMinute)

                    // 3. Define el formato deseado
                    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

                    // 4. Formatea el objeto LocalTime a un String
                    val formattedTime = localTime.format(formatter)

                    onTimeSelected(formattedTime)
                    onDismiss()
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        },
        title = {
            Text(
                text = "Seleccionar hora",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)
            )
        }
    ) {
        TimePicker(state = timePickerState)
    }
}