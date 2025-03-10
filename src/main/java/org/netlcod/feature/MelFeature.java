package org.netlcod.feature;

import org.apache.commons.math3.complex.Complex;

import static org.netlcod.filter.Filter.applyMelFilters;
import static org.netlcod.filter.Filter.mel;
import static org.netlcod.spectrum.Spectrum.stft;

public class MelFeature extends AudioFeatureConfiguration {

    /**
     * Compute a mel spectrogram.
     *
     * @param y Input signal.
     * @return Array of mel spectrogram.
     */
    public double[][] extract(double[] y) {
        Complex[][] spectrogram = stft(y, nFft, hopLength, "hann", nFft, true);

        int winSize = nFft / 2 + 1;
        int nFrames = y.length / winSize + 1;

        double[][] powerSpectrogram = new double[winSize][nFrames];

        for (int i = 0; i < winSize; i++) {
            for (int j = 0; j < nFrames; j++) {
                double real = spectrogram[i][j].getReal();
                double imag = spectrogram[i][j].getImaginary();
                powerSpectrogram[i][j] = real * real + imag * imag;
            }
        }

        double[][] melFilters = mel(sampleRate, nFft, featureSize, fMin, fMax, false);

        return applyMelFilters(powerSpectrogram, melFilters, featureSize, nFft);
    }
}
