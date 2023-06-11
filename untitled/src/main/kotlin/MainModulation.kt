import modulation.AmplitudeModulator
import modulation.FrequencyModulator
import modulation.SawtoothOscillator
import modulation.SineOscillator
import javax.sound.sampled.*
import kotlin.experimental.and

fun main(args: Array<String>) {
//    exampleAmplitudeModulation()
    exampleFrequencyModulation()
}

fun exampleAmplitudeModulation() {
    val carrierFrequency = 200.0  // Frequency of the carrier wave
    val modulationFrequency = 1.0 // Frequency of the modulation wave
    val duration = 3.0            // Duration of the generated waveform in seconds
    val sampleRate = 44100.0      // Sample rate (number of samples per second)

    val carrierOscillator = SineOscillator(carrierFrequency)
    val modulationOscillator = SineOscillator(modulationFrequency)

    val amplitudeModulator = AmplitudeModulator(carrierOscillator, modulationOscillator)
    val samples = amplitudeModulator.generateWaveform(duration, sampleRate)

    playSamples(samples, sampleRate)
}

fun exampleFrequencyModulation() {
    val carrierFrequency = 100.0  // Frequency of the carrier wave
    val modulationFrequency = 50.0 // Frequency of the modulation wave
    val duration = 10.0            // Duration of the generated waveform in seconds
    val sampleRate = 44100.0      // Sample rate (number of samples per second)

    val carrierOscillator = SineOscillator(carrierFrequency)
    val modulationOscillator = SawtoothOscillator(modulationFrequency)

    val frequencyModulator = FrequencyModulator(carrierOscillator, modulationOscillator)
    val samples = frequencyModulator.generateWaveform(duration, sampleRate)

    playSamples(samples, sampleRate)
}

fun playSamples(samples: DoubleArray, sampleRate: Double) {
    val audioFormat = AudioFormat(
        sampleRate.toFloat(),
        16, // Sample size in bits
        1,  // Channels (1 for mono, 2 for stereo)
        true, // Signed audio format
        false // Little endian byte order
    )

    val dataLineInfo = DataLine.Info(SourceDataLine::class.java, audioFormat)

    try {
        val line = AudioSystem.getLine(dataLineInfo) as SourceDataLine
        line.open(audioFormat)
        line.start()

        val buffer = ByteArray(2) // Buffer for 16-bit samples (2 bytes per sample)
        for (sample in samples) {
            val sampleValue = (sample * Short.MAX_VALUE).toInt().toShort()
            buffer[0] = (sampleValue and 0xFF).toByte()
            buffer[1] = ((sampleValue.toInt() shr 8) and 0xFF).toByte()
            line.write(buffer, 0, 2)
        }

        line.drain()
        line.stop()
        line.close()
    } catch (e: LineUnavailableException) {
        e.printStackTrace()
    }
}
