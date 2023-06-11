package modulation

class AmplitudeModulator(private val carrier: Oscillator, private val modulation: Oscillator) {
    fun generateWaveform(duration: Double, sampleRate: Double): DoubleArray {
        val numSamples = (duration * sampleRate).toInt()
        val samples = DoubleArray(numSamples)

        for (i in 0 until numSamples) {
            val time = i / sampleRate
            val carrierAmplitude = carrier.generateSample(time, 0.5)
            val modulationAmplitude = modulation.generateSample(time, 0.5)
            val amplitudeModulation = carrierAmplitude * modulationAmplitude

            samples[i] = amplitudeModulation
        }

        return samples
    }
}
