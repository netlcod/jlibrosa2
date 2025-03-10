package org.netlcod.feature;

import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;

import static org.netlcod.convert.Convert.powerToDB;
import static org.netlcod.filter.Filter.applyMelFilters;
import static org.netlcod.filter.Filter.mel;
import static org.netlcod.spectrum.Spectrum.dct;
import static org.netlcod.spectrum.Spectrum.stft;

public class MfccFeature extends AudioFeatureConfiguration {

    /**
     * Compute mel-frequency cepstral coefficients
     *
     * @param y Input signal.
     * @return Array of mel-frequency cepstral coefficients
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

        double[][] melFilters = mel(sampleRate, nFft, 128, fMin, fMax, false);

        double[][] melSpectrogram = applyMelFilters(powerSpectrogram, melFilters, 128, nFft);
        melSpectrogram = powerToDB(melSpectrogram, 1.0, 1e-10, 80.0);

        double[][] mfcc = dct(melSpectrogram);

        return Arrays.copyOf(mfcc, featureSize);
    }
}
