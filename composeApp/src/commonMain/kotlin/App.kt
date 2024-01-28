import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun App() {
    MaterialTheme {
        sightingPicker()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sightingPicker() {
    var showSightingPicker by remember { mutableStateOf(AddSighting.NONE) }
    var dateTime by remember { mutableStateOf(Clock.System.now()) }

    fun addBrief(chosenDateTime: Instant?) {
        if (chosenDateTime == null) {
            showSightingPicker = showSightingPicker.next()
            return
        }
        showSightingPicker = showSightingPicker.showHide()
        dateTime = chosenDateTime
    }

    fun addDate(chosenDate: Long) {
        showSightingPicker = showSightingPicker.next()
        dateTime = Instant.fromEpochMilliseconds(chosenDate)
    }

    fun addTime(chosenTime: Duration) {
        showSightingPicker = showSightingPicker.next()
        dateTime += chosenTime
    }

    fun back() {
        showSightingPicker = showSightingPicker.previous()
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showSightingPicker = showSightingPicker.showHide() }) {
            Text("Add Sighting")
        }
        Text(showSightingPicker.toString())
        Text(dateTime.toString())
        AnimatedVisibility(showSightingPicker == AddSighting.BRIEF) {
            SightingBriefPicker(::addBrief, ::back)
        }
        AnimatedVisibility(showSightingPicker == AddSighting.CALENDAR) {
            SightingDatePicker(::addDate, ::back)
        }
        AnimatedVisibility(showSightingPicker == AddSighting.TIMER) {
            SightingTimePicker(::addTime, ::back)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SightingBriefPicker(next: (chosenDate: Instant?) -> Unit, previous: () -> Unit) {
    val sightings = BriefSightings()
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(modifier = Modifier.fillMaxWidth(), onClick = { next(sightings.now) }) {
            Text("Now")
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { next(sightings.fiveMinutesAgo) }) {
            Text("Five Minutes Ago")
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { next(sightings.tenMinutesAgo) }) {
            Text("Ten Minutes Ago")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { next(sightings.fifteenMinutesAgo) }) {
            Text("Fifteen Minutes Ago")
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { next(null) }) {
            Text("Pick Custom Time")
        }
        Button(onClick = { previous() }) {
            Text("Cancel")
        }

    }
}

@ExperimentalMaterial3Api
@Composable
fun SightingDatePicker(next: (chosenDate: Long) -> Unit, previous: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        TODO: Some bakwas with timezones?
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
        )
        DatePicker(state = datePickerState, modifier = Modifier.padding(16.dp))
        val datePicked = datePickerState.selectedDateMillis!!
        ButtonRow(next = { next(datePicked) }, previous = previous)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingTimePicker(next: (chosenTime: Duration) -> Unit, previous: () -> Unit) {
    val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val timePickerState = rememberTimePickerState(
        initialHour = dateTime.hour, initialMinute = dateTime.minute, is24Hour = false
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePicker(state = timePickerState, modifier = Modifier.padding(16.dp))
        val timePicked = timePickerState.hour.hours + timePickerState.minute.minutes
        ButtonRow(next = { next(timePicked) }, previous = previous)
    }
}

@Composable
fun ButtonRow(next: () -> Unit, previous: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Button(modifier = Modifier.padding(16.dp), onClick = { previous() }) {
            Text("Back")
        }
        Button(modifier = Modifier.padding(16.dp), onClick = {
            next()
        }) {
            Text("Next")
        }
    }
}
