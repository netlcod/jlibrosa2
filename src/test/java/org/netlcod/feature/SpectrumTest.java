package org.netlcod.feature;

import org.apache.commons.math3.complex.Complex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.netlcod.spectrum.Spectrum.istft;
import static org.netlcod.spectrum.Spectrum.stft;


public class SpectrumTest {
    private static final double EPS = 1e-6;

    public static double[] generateSineWave(double frequency, int samplingRate, double duration) {
        int numSamples = (int) (samplingRate * duration);
        double[] sineWave = new double[numSamples];
        double[] time = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            time[i] = i / (double) samplingRate;
            sineWave[i] = Math.sin(2 * Math.PI * frequency * time[i]);
        }

        return sineWave;
    }

    @Test
    public void testStftWCenter() {
        double[] originalSignal = generateSineWave(440, 16000, 1.0);

        int nFft = 1024;
        int hopLength = 256;
        int winLength = 512;
        boolean center = true;
        String windowName = "hann";

        Complex[][] stftMatrix = stft(
                originalSignal,
                nFft,
                hopLength,
                windowName,
                winLength,
                center
        );

        double[] reconstructedSignal = istft(
                stftMatrix,
                nFft,
                hopLength,
                windowName,
                winLength,
                center
        );

        for (int i = 1; i < reconstructedSignal.length; i++) {
            assertEquals(originalSignal[i], reconstructedSignal[i], EPS, "Value " + i + " does not match");
        }

    }

    @Test
    public void testStftWOCenter() {
        double[] originalSignal = generateSineWave(440, 16000, 1.0);

        int nFft = 1024;
        int hopLength = 256;
        int winLength = 512;
        boolean center = false;
        String windowName = "hann";

        Complex[][] stftMatrix = stft(
                originalSignal,
                nFft,
                hopLength,
                windowName,
                winLength,
                center
        );

        double[] reconstructedSignal = istft(
                stftMatrix,
                nFft,
                hopLength,
                windowName,
                winLength,
                center
        );

        for (int i = winLength / 2 + 1; i < reconstructedSignal.length - winLength / 2; i++) {
            assertEquals(originalSignal[i], reconstructedSignal[i], EPS, "Value " + i + " does not match");
        }

    }
}