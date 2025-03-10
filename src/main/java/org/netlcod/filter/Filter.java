package org.netlcod.filter;

import static java.lang.Math.pow;
import static org.netlcod.convert.Convert.hzToMel;
import static org.netlcod.convert.Convert.melToHz;

public class Filter {

    /**
     * Create a Mel filter-bank
     *
     * @param sr    Sampling rate of the incoming signal (must be greater than 0).
     * @param nFft  Number of FFT components (must be greater than 0).
     * @param nMels Number of Mel bands to generate (default is 128).
     * @param fmin  Lowest frequency (in Hz, must be greater than or equal to 0).
     * @param fmax  Highest frequency (in Hz, must be greater than 0).
     * @param htk   If `true`, use the HTK formula for Mel scale conversion.
     *              Otherwise, use the Slaney formula.
     * @return A Mel transform matrix of shape (nMels, 1 + nFft / 2).
     */
    public static double[][] mel(double sr, int nFft, int nMels, double fmin, double fmax, boolean htk) {
        double[] fftFrequencies = fftFrequencies(sr, nFft);
        double[] melFrequencies = melFrequencies(nMels + 2, fmin, fmax, htk);

        double[] fdiff = new double[melFrequencies.length - 1];
        for (int i = 0; i < melFrequencies.length - 1; i++) {
            fdiff[i] = melFrequencies[i + 1] - melFrequencies[i];
        }

        double[][] weights = new double[nMels][1 + nFft / 2];

        for (int i = 0; i < nMels; i++) {
            for (int j = 0; j < fftFrequencies.length; j++) {
                double lower = -(melFrequencies[i] - fftFrequencies[j]) / fdiff[i];
                double upper = (melFrequencies[i + 2] - fftFrequencies[j]) / fdiff[i + 1];
                weights[i][j] = Math.max(0, Math.min(lower, upper));
            }
        }

        if (htk) {
            for (int i = 0; i < nMels; i++) {
                for (int j = 0; j < fftFrequencies.length; j++) {
                    weights[i][j] = 700.0 * (pow(10.0, weights[i][j] / 2595.0) - 1.0);
                }
            }
        } else {
            double[] enorm = new double[nMels];
            for (int i = 0; i < nMels; i++) {
                enorm[i] = 2.0 / (melFrequencies[i + 2] - melFrequencies[i]);
                for (int j = 0; j < fftFrequencies.length; j++) {
                    weights[i][j] *= enorm[i];
                }
            }
        }

        return weights;
    }

    /**
     * Apply mel-filters to spectrogram.
     *
     * @param spectrogram Spectrogram.
     * @param melFilters  Mel-filters.
     * @return MelSpectrogram.
     */
    public static double[][] applyMelFilters(double[][] spectrogram, double[][] melFilters, int nMels, int nFft) {
        int nFrames = spectrogram[0].length;
        double[][] melSpectrogram = new double[nMels][nFrames];

        for (int i = 0; i < nMels; i++) {
            for (int j = 0; j < nFrames; j++) {
                double sum = 0;
                for (int k = 0; k < 1 + nFft / 2; k++) {
                    sum += melFilters[i][k] * spectrogram[k][j];
                }
                melSpectrogram[i][j] = sum;
            }
        }

        return melSpectrogram;
    }

    /**
     * Compute the center frequencies of FFT bins.
     *
     * @param sr   Audio sampling rate.
     * @param nFft FFT window size.
     * @return Array of FFT bin frequencies.
     */
    public static double[] fftFrequencies(double sr, int nFft) {
        double[] freqs = new double[nFft / 2 + 1];
        for (int i = 0; i < freqs.length; i++) {
            freqs[i] = i * sr / nFft;
        }
        return freqs;
    }

    /**
     * Compute the center frequencies of mel bins.
     *
     * @param nMel Number of mel bins.
     * @param fMin Minimum frequency (Hz).
     * @param fMax Maximum frequency (Hz).
     * @param htk  If true, use HTK formula instead of Slaney.
     * @return Array of mel bin frequencies.
     */
    public static double[] melFrequencies(int nMel, double fMin, double fMax, boolean htk) {
        double[] mels = new double[nMel];
        double minMel = hzToMel(new double[]{fMin}, htk)[0];
        double maxMel = hzToMel(new double[]{fMax}, htk)[0];
        double deltaMel = (maxMel - minMel) / (nMel - 1);

        for (int i = 0; i < nMel; i++) {
            mels[i] = minMel + i * deltaMel;
        }

        return melToHz(mels, htk);
    }
}
