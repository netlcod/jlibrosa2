package org.netlcod.window;


public class Window {

    /**
     * Generates a Bartlett (triangular) window.
     *
     * @param size    Size of the window.
     * @param fftbins If true, create a periodic window for use with FFT.
     *                If false, create a symmetric window for filter design applications.
     * @return An array representing the Bartlett window.
     */
    public static double[] bartlett(int size, boolean fftbins) {
        double[] window = new double[size];
        int N = fftbins ? size : size - 1;
        for (int i = 0; i < size; i++) {
            if (i <= N / 2) {
                window[i] = 2.0 * i / N;
            } else {
                window[i] = 2.0 - 2.0 * i / N;
            }
        }
        return window;
    }

    /**
     * Generates a Blackman window.
     *
     * @param size Size of the window.
     * @param fftbins If true, create a periodic window for use with FFT.
     *                If false, create a symmetric window for filter design applications.
     * @return An array representing the Blackman window.
     */
    public static double[] blackman(int size, boolean fftbins) {
        double[] window = new double[size];
        int N = fftbins ? size : size - 1;
        double alpha = 0.16; // Standard value for the Blackman window
        double a0 = (1 - alpha) / 2;
        double a1 = 0.5;
        double a2 = alpha / 2;

        for (int i = 0; i < size; i++) {
            window[i] = a0 - a1 * Math.cos(2 * Math.PI * i / N) + a2 * Math.cos(4 * Math.PI * i / N);
        }
        return window;
    }

    /**
     * Generates a Hamming window.
     *
     * @param size Size of the window.
     * @param fftbins If true, create a periodic window for use with FFT.
     *                If false, create a symmetric window for filter design applications.
     * @return An array representing the Hamming window.
     */
    public static double[] hamming(int size, boolean fftbins) {
        double[] window = new double[size];
        int N = fftbins ? size : size - 1;
        double alpha = 0.54;
        double beta = 1 - alpha;

        for (int i = 0; i < size; i++) {
            window[i] = alpha - beta * Math.cos(2 * Math.PI * i / N);
        }
        return window;
    }

    /**
     * Generates a Hanning (Hann) window.
     *
     * @param size Size of the window.
     * @param fftbins If true, create a periodic window for use with FFT.
     *                If false, create a symmetric window for filter design applications.
     * @return An array representing the Hanning window.
     */
    public static double[] hann(int size, boolean fftbins) {
        double[] window = new double[size];
        int N = fftbins ? size : size - 1;

        for (int i = 0; i < size; i++) {
            window[i] = 0.5 * (1 - Math.cos(2 * Math.PI * i / N));
        }
        return window;
    }

    /**
     * Generates a Welch window.
     *
     * @param size Size of the window.
     * @param fftbins If true, create a periodic window for use with FFT.
     *                If false, create a symmetric window for filter design applications.
     * @return An array representing the Welch window.
     */
    public static double[] welch(int size, boolean fftbins) {
        double[] window = new double[size];
        int N = fftbins ? size : size - 1;

        for (int i = 0; i < size; i++) {
            double term = (i - N / 2.0) / (N / 2.0);
            window[i] = 1 - term * term;
        }
        return window;
    }

    /**
     * Compute a window function.
     *
     * @param windowName Name of the window.
     * @param size       Size of the window.
     * @param fftbins If true, create a periodic window for use with FFT.
     *                If false, create a symmetric window for filter design applications.
     * @return An array representing the Welch window.
     */
    public static double[] getWindow(String windowName, int size, boolean fftbins) {
        double[] window = new double[size];
        switch (windowName.toLowerCase()) {
            case "bartlett":
                window = bartlett(size, fftbins);
                break;
            case "blackman":
                window = blackman(size, fftbins);
                break;
            case "hamming":
                window = hamming(size, fftbins);
                break;
            case "hann":
                window = hann(size, fftbins);
                break;
            case "welch":
                window = welch(size, fftbins);
                break;
            default:
                throw new IllegalArgumentException("Unsupported window type: " + window);
        }

        return window;
    }
}