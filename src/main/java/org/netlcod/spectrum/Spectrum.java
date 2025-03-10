package org.netlcod.spectrum;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;

import static org.netlcod.window.Window.getWindow;

public class Spectrum {

    /**
     * Performs the Short-Time Fourier Transform (STFT) of the input signal.
     *
     * @param y          The input signal.
     * @param nFft       The FFT window size.
     * @param hopLength  The hop length between frames.
     * @param windowName The window function (e.g., "hann").
     * @param winLength  The window length.
     * @param center     If true, the signal is padded so that frames are centered.
     * @return A 2D array representing the complex STFT matrix.
     */
    public static Complex[][] stft(double[] y, int nFft, int hopLength, String windowName, int winLength, boolean center) {
        double[] data = y.clone();

        // 1
        double[] window = getWindow(windowName, winLength, true);
        window = pad(window, (nFft - winLength) / 2, (nFft - winLength) / 2, "constant");

        // 2
        if (center) {
            int padding = nFft / 2;
            data = pad(data, padding, padding, "constant");
        }

        // 3
        int nFrames;
        if (center) {
            nFrames = (data.length - nFft) / hopLength + 1;
        } else {
            nFrames = (data.length - nFft + hopLength) / hopLength;
        }
        int fftBins = nFft / 2 + 1;

        // 4
        Complex[][] stftMatrix = new Complex[fftBins][nFrames];
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        for (int t = 0; t < nFrames; t++) {
            double[] frame = new double[nFft];
            int start = t * hopLength;

            System.arraycopy(data, start, frame, 0, Math.min(nFft, data.length - start));

            for (int i = 0; i < frame.length; i++) {
                frame[i] *= window[i];
            }

            Complex[] fftResult = fft.transform(frame, TransformType.FORWARD);

            for (int f = 0; f < fftBins; f++) {
                stftMatrix[f][t] = fftResult[f];
            }
        }

        return stftMatrix;
    }

    /**
     * Performs the Inverse Short-Time Fourier Transform (ISTFT) to reconstruct a time-domain signal.
     *
     * @param stftMatrix Input spectrogram matrix with shape [frequencyBins][frames].
     * @param nFft       FFT window size (number of samples per frame).
     * @param hopLength  Number of samples between successive frames.
     * @param windowName Name of the window function to use ("hann", "hamming", etc.)
     * @param winLength  Length of the window function.
     * @param center     If true, the signal is padded so that frames are centered.
     * @return Array representing the reconstructed time-domain signal
     */
    public static double[] istft(Complex[][] stftMatrix,
                                 int nFft,
                                 int hopLength,
                                 String windowName,
                                 int winLength,
                                 boolean center) {
        // 1
        double[] window = getWindow(windowName, winLength, true);
        window = pad(window, (nFft - winLength) / 2, (nFft - winLength) / 2, "constant");

        // 2
        int nFrames = stftMatrix[0].length;

        // 3
        int expectedLength = nFft + hopLength * (nFrames - 1);
        if (center) {
            expectedLength -= nFft;
        }

        double[] y = new double[expectedLength];

        // 4
        int startFrame = 0;
        int offset = 0;
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        for (int frame = startFrame; frame < nFrames; frame++) {
            Complex[] column = new Complex[stftMatrix.length];
            for (int i = 0; i < column.length; i++) {
                column[i] = stftMatrix[i][frame];
            }

            Complex[] padded = new Complex[nFft];
            System.arraycopy(column, 0, padded, 0, column.length);
            for (int i = 1; i < column.length; i++) {
                padded[nFft - i] = column[i].conjugate();
            }

            Complex[] ifftResult = fft.transform(padded, TransformType.INVERSE);

            double[] windowed = new double[nFft];
            for (int i = 0; i < nFft; i++) {
                windowed[i] = ifftResult[i].getReal() * window[i];
            }

            // Overlap-add
            int pos = frame * hopLength + offset - (center ? nFft / 2 : 0);
            for (int i = 0; i < nFft; i++) {
                if (pos + i >= 0 && pos + i < y.length) {
                    y[pos + i] += windowed[i];
                }
            }
        }

        // 6
        double[] windowSum = windowSumSquare(window, nFft, hopLength, nFrames);
        if (center) {
            windowSum = Arrays.copyOfRange(windowSum, nFft / 2, windowSum.length);
        }

        // 7
        for (int i = 0; i < y.length; i++) {
            if (windowSum[i] > 1e-15) {
                y[i] /= windowSum[i];
            }
        }

        return y;
    }

    /**
     * Compute the Discrete Cosine Transform (DCT) of the input.
     *
     * @param y The input.
     * @return A 2D array representing the DCT coefficients.
     */
    public static double[][] dct(double[][] y) {
        int rows = y.length;
        int cols = y[0].length;
        double[][] result = new double[rows][cols];

        for (int c = 0; c < cols; c++) {
            double[] column = new double[rows];
            for (int r = 0; r < rows; r++) {
                column[r] = y[r][c];
            }

            double[] dctColumn = dct(column);

            for (int r = 0; r < rows; r++) {
                result[r][c] = dctColumn[r];
            }
        }

        return result;
    }

    /**
     * Compute the Discrete Cosine Transform (DCT) of the input.
     *
     * @param y The input spectrogram.
     * @return A 2D array representing the DCT coefficients.
     */
    public static double[] dct(double[] y) {
        int N = y.length;
        double[] extendedInput = new double[2 * N];

        for (int i = 0; i < N; i++) {
            extendedInput[i] = y[i];
            extendedInput[2 * N - 1 - i] = y[i];
        }

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fftResult = fft.transform(extendedInput, TransformType.FORWARD);

        double[] dctResult = new double[N];
        for (int i = 0; i < N; i++) {
            double theta = Math.PI * i / (2 * N);
            Complex mul = new Complex(Math.cos(theta), -Math.sin(theta));
            dctResult[i] = fftResult[i].multiply(mul).getReal();
        }

        dctResult[0] *= Math.sqrt(1.0 / N) / 2;
        for (int i = 1; i < N; i++) {
            dctResult[i] *= Math.sqrt(2.0 / N) / 2;
        }

        return dctResult;
    }

    /**
     * Compute the sum-square envelope of a window function at a given hop length.
     *
     * @param window    Window array.
     * @param nFft      Length of each analysis frame.
     * @param hopLength Number of samples between successive frames.
     * @param nFrames   Number of analysis frames.
     * @return Array representing the sum-squared envelope of the window function
     */
    private static double[] windowSumSquare(double[] window, int nFft, int hopLength, int nFrames) {
        int expectedLength = nFft + hopLength * (nFrames - 1);
        double[] sum = new double[expectedLength];

        for (int frame = 0; frame < nFrames; frame++) {
            int pos = frame * hopLength;

            for (int i = 0; i < window.length; i++) {
                int idx = pos + i;
                if (idx >= 0 && idx < sum.length) {
                    sum[idx] += window[i] * window[i]; // Сумма квадратов окон
                }
            }
        }

        return sum;
    }

    /**
     * Adds padding to the signal.
     *
     * @param y        The input signal.
     * @param leftPad  The length of the left part window.
     * @param rightPad The length of the right part window.
     * @param padMode  Padding mode [constant, reflect, wrap, edge].
     * @return The padded signal.
     */
    public static double[] pad(double[] y,
                               int leftPad,
                               int rightPad,
                               String padMode) {
        double[] padded = new double[y.length + leftPad + rightPad];

        switch (padMode.toLowerCase()) {
            case "constant":
                // Добавляем нули по умолчанию
                Arrays.fill(padded, 0, leftPad, 0.0);
                Arrays.fill(padded, y.length + leftPad, padded.length, 0.0);
                System.arraycopy(y, 0, padded, leftPad, y.length);
                break;

            case "reflect":
                // Зеркальное отражение
                for (int i = 0; i < leftPad; i++) {
                    padded[leftPad - 1 - i] = y[i % y.length];
                }
                for (int i = 0; i < rightPad; i++) {
                    padded[y.length + leftPad + i] = y[y.length - 1 - (i % y.length)];
                }
                System.arraycopy(y, 0, padded, leftPad, y.length);
                break;

            case "wrap":
                // Циклическое повторение
                for (int i = 0; i < leftPad; i++) {
                    padded[i] = y[(y.length - (leftPad - i)) % y.length];
                }
                for (int i = 0; i < rightPad; i++) {
                    padded[y.length + leftPad + i] = y[i % y.length];
                }
                System.arraycopy(y, 0, padded, leftPad, y.length);
                break;

            case "edge":
                // Повторение крайних элементов
                Arrays.fill(padded, 0, leftPad, y[0]);
                Arrays.fill(padded, y.length + leftPad, padded.length, y[y.length - 1]);
                System.arraycopy(y, 0, padded, leftPad, y.length);
                break;

            default:
                throw new IllegalArgumentException("Unsupported pad mode: " + padMode);
        }

        return padded;
    }
}