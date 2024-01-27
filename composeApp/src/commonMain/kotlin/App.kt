import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        var showCalender by remember { mutableStateOf(false) }
        var showTimer by remember { mutableStateOf(false) }
        var dateTime by remember { mutableLongStateOf(0) }

        fun addDate(chosenDate: Long) {
            showCalender = false
            showTimer = true
            dateTime = chosenDate
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showCalender = !showCalender; showTimer = false }) {
                Text("Add Sighting")
            }
            AnimatedVisibility(showCalender) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SightingDatePicker(::addDate)
                }
            }
            AnimatedVisibility(showTimer) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SightingTimePicker(dateTime)
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SightingDatePicker(onClick: (chosenDate: Long) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds())
        DatePicker(state = datePickerState, modifier = Modifier.padding(16.dp))

        Text("Selected date timestamp: ${datePickerState.selectedDateMillis ?: "no selection"}", modifier = Modifier.padding(16.dp))
        if (datePickerState.selectedDateMillis != null) Button(onClick = { onClick(datePickerState.selectedDateMillis!!) }) {
            Text("Select Date")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingTimePicker(chosenDate: Long) {
    val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val timePicker = remember {
        TimePickerState(
            initialHour = dateTime.hour,
            initialMinute = dateTime.minute,
            is24Hour = false
        )
    }
    Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TimePicker(state = timePicker)

        Text("Selected date timestamp: $chosenDate", modifier = Modifier.padding(16.dp))
        Text("Selected time timestamp: ${TimePickerState}", modifier = Modifier.padding(16.dp))
    }

}
