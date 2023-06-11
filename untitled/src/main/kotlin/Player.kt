import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.experimental.and
import kotlin.math.pow

abstract class Player(
    private val oscillator: Oscillator,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val notes: List<Note> = emptyList(),
    protected val metronome: Metronome = Metronome()
) {
    private var playing = true

    abstract fun playNote(note: Note, playAt: LocalDateTime)

    fun playNotes(at: LocalDateTime) {
        notes.forEach { note ->
            val playAt = at.plus((note.beat * metronome.millisPerBeat).toLong(), ChronoUnit.MILLIS)
            schedule(playAt) {
                if (playing) {
                    playNote(note, playAt)
                }
            }
        }
    }

    fun schedule(time: LocalDateTime, function: () -> Unit) {
        scope.launch {
            delay(Duration.between(LocalDateTime.now(), time).toMillis())
            function.invoke()
        }
    }

    fun generateBuffer(frequency: Double): ByteArray {
        val sampleRate = 44100.0
        val numSamples = (0.25 * sampleRate).toInt()

        val buffer = ByteArray(numSamples * 2)

//        for (i in 0 until numSamples) {
//            val t = i / sampleRate
//            val value = oscillator.generateSample(frequency, t)
//
//            buffer[i * 2] = (value and 0xff).toByte()
//            buffer[i * 2 + 1] = (value.toInt() shr 8 and 0xff).toByte()
//        }

        return buffer
    }

    fun calculateFrequency(note: Int): Double {
        val freqA = 440.0
        val noteA = 69

        return freqA * 2.0.pow((note - noteA) / 12.0)
    }
}