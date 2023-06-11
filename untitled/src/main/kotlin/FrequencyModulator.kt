import kotlin.math.PI
import kotlin.math.sin

class FrequencyModulator(private val carrier: Oscillator, private val modulation: Oscillator) {
    fun generateWaveform(duration: Double, sampleRate: Double): DoubleArray {
        val numSamples = (duration * sampleRate).toInt()
        val samples = DoubleArray(numSamples)

        for (i in 0 until numSamples) {
            val time = i / sampleRate
            val carrierFrequency = carrier.frequency + modulation.generateSample(time, 100.0)
            val carrierAmplitude = carrier.generateSample(time, 0.5)
            val angle = 2.0 * PI * carrierFrequency * time
            val frequencyModulation = carrierAmplitude * sin(angle)

            samples[i] = frequencyModulation
        }

        return samples
    }
}
