package org.netlcod.convert;


public class Convert {
    /**
     * Convert frame indices to audio sample indices.
     *
     * @param frames    Array of frame indices.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Array of sample indices corresponding to each frame.
     */
    public static int[] framesToSamples(int[] frames, int hopLength, int offset) {
        int[] samples = new int[frames.length];
        for (int i = 0; i < frames.length; i++) {
            samples[i] = framesToSamples(frames[i], hopLength, offset);
        }
        return samples;
    }

    /**
     * Convert frame indices to audio sample indices.
     *
     * @param frame     Frame index.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Number of sample index corresponding to frame.
     */
    public static int framesToSamples(int frame, int hopLength, int offset) {
        return frame * hopLength + offset;
    }

    /**
     * Convert sample indices into STFT frames.
     *
     * @param samples   Array of sample indices.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Array of frame indices corresponding to each sample.
     */
    public static int[] samplesToFrames(int[] samples, int hopLength, int offset) {
        int[] frames = new int[samples.length];
        for (int i = 0; i < samples.length; i++) {
            frames[i] = samplesToFrames(samples[i], hopLength, offset);
        }
        return frames;
    }

    /**
     * Convert sample indices into STFT frames.
     *
     * @param sample    Sample index.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Number of frame index corresponding to sample.
     */
    public static int samplesToFrames(int sample, int hopLength, int offset) {
        return (int) Math.floor((sample - offset) / (double) hopLength);
    }

    /**
     * Convert frame counts to time (seconds).
     *
     * @param frames    Array of frame indices.
     * @param sr        Audio sampling rate.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Array of time values corresponding to each frame.
     */
    public static double[] framesToTime(int[] frames, double sr, int hopLength, int offset) {
        int[] samples = framesToSamples(frames, hopLength, offset);
        return samplesToTime(samples, sr);
    }

    /**
     * Convert frame counts to time (seconds).
     *
     * @param frame     Frame index.
     * @param sr        Audio sampling rate.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Number of time value corresponding to frame.
     */
    public static double framesToTime(int frame, double sr, int hopLength, int offset) {
        int sample = framesToSamples(frame, hopLength, offset);
        return samplesToTime(sample, sr);
    }

    /**
     * Convert time stamps into STFT frames.
     *
     * @param times     Array of time values.
     * @param sr        Audio sampling rate.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Array of frame indices corresponding to each time value.
     */
    public static int[] timeToFrames(double[] times, double sr, int hopLength, int offset) {
        int[] samples = timeToSamples(times, sr);
        return samplesToFrames(samples, hopLength, offset);
    }

    /**
     * Convert time stamps into STFT frames.
     *
     * @param time      Time (in seconds).
     * @param sr        Audio sampling rate.
     * @param hopLength Number of samples between successive frames.
     * @param offset    Offset to counteract windowing effects when using a non-centered STFT [nFft // 2].
     * @return Number of frame index corresponding to time value.
     */
    public static int timeToFrames(double time, double sr, int hopLength, int offset) {
        int sample = timeToSamples(time, sr);
        return samplesToFrames(sample, hopLength, offset);
    }

    /**
     * Convert timestamps (in seconds) to sample indices.
     *
     * @param times Array of time values (in seconds).
     * @param sr    Sampling rate.
     * @return Array of sample indices corresponding to each time value.
     */
    public static int[] timeToSamples(double[] times, double sr) {
        int[] samples = new int[times.length];
        for (int i = 0; i < times.length; i++) {
            samples[i] = timeToSamples(times[i], sr);
        }
        return samples;
    }

    /**
     * Convert timestamps (in seconds) to sample indices.
     *
     * @param time Time value or array of time values (in seconds).
     * @param sr   Sampling rate.
     * @return Number of sample index corresponding to time value.
     */
    public static int timeToSamples(double time, double sr) {
        return (int) (time * sr);
    }

    /**
     * Convert sample indices to time (in seconds).
     *
     * @param samples Array of sample indices.
     * @param sr      Sampling rate.
     * @return Array of time values corresponding to each sample.
     */
    public static double[] samplesToTime(int[] samples, double sr) {
        double[] times = new double[samples.length];
        for (int i = 0; i < samples.length; i++) {
            times[i] = samplesToTime(samples[i], sr);
        }
        return times;
    }

    /**
     * Convert sample indices to time (in seconds).
     *
     * @param sample Sample index.
     * @param sr     Sampling rate.
     * @return Number of time value corresponding to sample.
     */
    public static double samplesToTime(int sample, double sr) {
        return sample / sr;
    }

    /**
     * Convert block indices to frame indices.
     *
     * @param blocks      Array of block indices.
     * @param blockLength The number of frames per block.
     * @return Array of frame indices corresponding to each block.
     */
    public static int[] blocksToFrames(int[] blocks, int blockLength) {
        int[] frames = new int[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            frames[i] = blocksToFrames(blocks[i], blockLength);
        }
        return frames;
    }

    /**
     * Convert block indices to frame indices.
     *
     * @param block       Block index.
     * @param blockLength The number of frames per block.
     * @return Number of frame index corresponding to block.
     */
    public static int blocksToFrames(int block, int blockLength) {
        return block * blockLength;
    }

    /**
     * Convert block indices to sample indices.
     *
     * @param blocks      Array of block indices.
     * @param blockLength The number of frames per block.
     * @param hopLength   The number of samples to advance between frames.
     * @return Array of sample indices corresponding to each block.
     */
    public static int[] blocksToSamples(int[] blocks, int blockLength, int hopLength) {
        int[] frames = blocksToFrames(blocks, blockLength);
        return framesToSamples(frames, hopLength, 0);
    }

    /**
     * Convert block indices to sample indices.
     *
     * @param block       Block index.
     * @param blockLength The number of frames per block.
     * @param hopLength   The number of samples to advance between frames.
     * @return Number of sample index corresponding to block.
     */
    public static int blocksToSamples(int block, int blockLength, int hopLength) {
        int frame = blocksToFrames(block, blockLength);
        return framesToSamples(frame, hopLength, 0);
    }

    /**
     * Convert block indices to time (in seconds).
     *
     * @param blocks      Array of block indices.
     * @param blockLength The number of frames per block.
     * @param hopLength   The number of samples to advance between frames.
     * @param sr          The sampling rate (samples per second).
     * @return Array of time values corresponding to each block.
     */
    public static double[] blocksToTime(int[] blocks, int blockLength, int hopLength, double sr) {
        int[] samples = blocksToSamples(blocks, blockLength, hopLength);
        return samplesToTime(samples, sr);
    }

    /**
     * Convert block indices to time (in seconds).
     *
     * @param block       Block index.
     * @param blockLength The number of frames per block.
     * @param hopLength   The number of samples to advance between frames.
     * @param sr          The sampling rate (samples per second).
     * @return Number of time value corresponding to block.
     */
    public static double blocksToTime(int block, int blockLength, int hopLength, double sr) {
        int sample = blocksToSamples(block, blockLength, hopLength);
        return samplesToTime(sample, sr);
    }

    /**
     * Convert frequency (Hz) to mel scale.
     *
     * @param frequencies Array of frequencies (in Hz).
     * @param htk         If true, use HTK formula instead of Slaney.
     * @return Array of mel values corresponding to each frequency.
     */
    public static double[] hzToMel(double[] frequencies, boolean htk) {
        double[] mels = new double[frequencies.length];
        for (int i = 0; i < frequencies.length; i++) {
            mels[i] = hzToMel(frequencies[i], htk);
        }
        return mels;
    }

    /**
     * Convert frequency (Hz) to mel scale.
     *
     * @param frequency Frequency (in Hz).
     * @param htk       If true, use HTK formula instead of Slaney.
     * @return Number of mel value corresponding to frequency.
     */
    public static double hzToMel(double frequency, boolean htk) {
        double mel;
        if (htk) {
            mel = 2595.0 * Math.log10(1.0 + frequency / 700.0);
        } else {
            double f_min = 0.0;
            double f_sp = 200.0 / 3;
            double min_log_hz = 1000.0;
            double min_log_mel = (min_log_hz - f_min) / f_sp;
            double logstep = Math.log(6.4) / 27.0;

            if (frequency >= min_log_hz) {
                mel = min_log_mel + Math.log(frequency / min_log_hz) / logstep;
            } else {
                mel = (frequency - f_min) / f_sp;
            }
        }
        return mel;
    }

    /**
     * Convert mel scale to frequency (Hz).
     *
     * @param mels Array of mel values.
     * @param htk  If true, use HTK formula instead of Slaney.
     * @return Array of frequencies corresponding to each mel value.
     */
    public static double[] melToHz(double[] mels, boolean htk) {
        double[] frequencies = new double[mels.length];
        for (int i = 0; i < mels.length; i++) {
            frequencies[i] = melToHz(mels[i], htk);
        }
        return frequencies;
    }

    /**
     * Convert mel scale to frequency (Hz).
     *
     * @param mel Mel value.
     * @param htk If true, use HTK formula instead of Slaney.
     * @return Number of frequency corresponding to mel value.
     */
    public static double melToHz(double mel, boolean htk) {
        double frequency;
        if (htk) {
            frequency = 700.0 * (Math.pow(10.0, mel / 2595.0) - 1.0);
        } else {
            double f_min = 0.0;
            double f_sp = 200.0 / 3;
            double min_log_hz = 1000.0;
            double min_log_mel = (min_log_hz - f_min) / f_sp;
            double logstep = Math.log(6.4) / 27.0;

            if (mel >= min_log_mel) {
                frequency = min_log_hz * Math.exp(logstep * (mel - min_log_mel));
            } else {
                frequency = f_min + f_sp * mel;
            }
        }
        return frequency;
    }

    /**
     * Convert a power spectrogram to decibel (dB) units.
     *
     * @param S     The power spectrogram.
     * @param ref   The reference value for dB scaling.
     * @param amin  The minimum threshold for numerical stability.
     * @param topDB The threshold for clipping the output.
     * @return The dB-scaled spectrogram.
     */
    public static double[][] powerToDB(double[][] S, double ref, double amin, Double topDB) {
        int nFreq = S.length;
        int nTime = S[0].length;
        double[][] SDB = new double[nFreq][nTime];

        for (int f = 0; f < nFreq; f++) {
            for (int t = 0; t < nTime; t++) {
                SDB[f][t] = 10 * Math.log10(Math.max(amin, S[f][t]) / ref);
            }
        }

        if (topDB != null) {
            double maxDB = Double.NEGATIVE_INFINITY;
            for (int f = 0; f < nFreq; f++) {
                for (int t = 0; t < nTime; t++) {
                    if (SDB[f][t] > maxDB) {
                        maxDB = SDB[f][t];
                    }
                }
            }
            double threshold = maxDB - topDB;
            for (int f = 0; f < nFreq; f++) {
                for (int t = 0; t < nTime; t++) {
                    SDB[f][t] = Math.max(SDB[f][t], threshold);
                }
            }
        }

        return SDB;
    }

    /**
     * Convert a dB-scaled spectrogram back to power units.
     *
     * @param SDB The dB-scaled spectrogram.
     * @param ref The reference value used for dB scaling.
     * @return The power spectrogram.
     */
    public static double[][] dbToPower(double[][] SDB, double ref) {
        int nFreq = SDB.length;
        int nTime = SDB[0].length;
        double[][] S = new double[nFreq][nTime];

        for (int f = 0; f < nFreq; f++) {
            for (int t = 0; t < nTime; t++) {
                S[f][t] = ref * Math.pow(10, SDB[f][t] / 10);
            }
        }

        return S;
    }

    /**
     * Convert an amplitude spectrogram to decibel (dB) units.
     *
     * @param S     The amplitude spectrogram.
     * @param ref   The reference value for dB scaling.
     * @param amin  The minimum threshold for numerical stability.
     * @param topDB The threshold for clipping the output.
     * @return The dB-scaled spectrogram.
     */
    public static double[][] amplitudeToDB(double[][] S, double ref, double amin, Double topDB) {
        int nFreq = S.length;
        int nTime = S[0].length;
        double[][] SDB = new double[nFreq][nTime];

        for (int f = 0; f < nFreq; f++) {
            for (int t = 0; t < nTime; t++) {
                SDB[f][t] = 20 * Math.log10(Math.max(amin, S[f][t]) / ref);
            }
        }

        if (topDB != null) {
            double maxDB = Double.NEGATIVE_INFINITY;
            for (int f = 0; f < nFreq; f++) {
                for (int t = 0; t < nTime; t++) {
                    if (SDB[f][t] > maxDB) {
                        maxDB = SDB[f][t];
                    }
                }
            }
            double threshold = maxDB - topDB;
            for (int f = 0; f < nFreq; f++) {
                for (int t = 0; t < nTime; t++) {
                    SDB[f][t] = Math.max(SDB[f][t], threshold);
                }
            }
        }

        return SDB;
    }

    /**
     * Convert a dB-scaled spectrogram back to amplitude units.
     *
     * @param SDB The dB-scaled spectrogram.
     * @param ref The reference value used for dB scaling.
     * @return The amplitude spectrogram.
     */
    public static double[][] dbToAmplitude(double[][] SDB, double ref) {
        int nFreq = SDB.length;
        int nTime = SDB[0].length;
        double[][] S = new double[nFreq][nTime];

        for (int f = 0; f < nFreq; f++) {
            for (int t = 0; t < nTime; t++) {
                S[f][t] = ref * Math.pow(10, SDB[f][t] / 20);
            }
        }

        return S;
    }
}
