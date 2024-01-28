import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

data class ShowAddSighting(
    val showBrief: Boolean = false,
    val showCalendar: Boolean = false,
    val showTimer: Boolean = false,
)

enum class AddSighting {
    NONE, BRIEF, CALENDAR, TIMER;
    fun next(): AddSighting {
        return when (this) {
            NONE -> BRIEF
            BRIEF -> CALENDAR
            CALENDAR -> TIMER
            TIMER -> NONE
        }
    }

    fun previous(): AddSighting {
        return when (this) {
            NONE -> NONE
            BRIEF -> NONE
            CALENDAR -> BRIEF
            TIMER -> CALENDAR
        }
    }

    fun showHide(): AddSighting {
        if (this == NONE) return this.next()
        return NONE
    }
}

data class BriefSightings (
    val now: Instant = Clock.System.now(),
    val fiveMinutesAgo: Instant = now.minus(5.minutes),
    val tenMinutesAgo: Instant = now.minus(10.minutes),
    val fifteenMinutesAgo: Instant = now.minus(15.minutes)
)