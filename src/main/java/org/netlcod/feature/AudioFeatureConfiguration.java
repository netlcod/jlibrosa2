package org.netlcod.feature;

public abstract class AudioFeatureConfiguration {
    protected int sampleRate;
    protected double fMin;
    protected double fMax;
    protected int nFft;
    protected int hopLength;
    protected int featureSize;

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        this.fMax = sampleRate / 2.0;
    }

    /**
     * Returns the minimum frequency (fMin) used in the filter bank.
     *
     * @return the minimum frequency (fMin)
     */
    public double getFMin() {
        return fMin;
    }

    /**
     * Sets the minimum frequency (fMin) used in the filter bank.
     *
     * @param fMin the new minimum frequency (fMin)
     */
    public void setFMin(double fMin) {
        this.fMin = fMin;
    }

    /**
     * Returns the maximum frequency (fMax) used in the filter bank.
     *
     * @return the maximum frequency (fMax)
     */
    public double getFMax() {
        return fMax;
    }

    /**
     * Sets the maximum frequency (fMax) used in the filter bank.
     *
     * @param fMax the new maximum frequency (fMax)
     */
    public void setFMax(double fMax) {
        this.fMax = fMax;
    }

    /**
     * Returns the number of FFT components (nFft) used in the filter bank.
     *
     * @return the number of FFT components (nFft)
     */
    public int getNFft() {
        return nFft;
    }

    /**
     * Sets the number of FFT components (nFft) used in the filter bank.
     *
     * @param nFft the new number of FFT components (nFft)
     */
    public void setNFft(int nFft) {
        this.nFft = nFft;
    }

    /**
     * Returns the hop length (hopLength) used in the filter bank.
     *
     * @return the hop length (hopLength)
     */
    public int getHopLength() {
        return hopLength;
    }

    /**
     * Sets the hop length (hopLength) used in the filter bank.
     *
     * @param hopLength the new hop length (hopLength)
     */
    public void setHopLength(int hopLength) {
        this.hopLength = hopLength;
    }

    /**
     * Returns the size of the feature (featureSize) used in the filter bank.
     *
     * @return the size of the feature (featureSize)
     */
    public int getFeatureSize() {
        return featureSize;
    }

    /**
     * Sets the size of the feature (featureSize) used in the filter bank.
     *
     * @param featureSize the new size of the feature (featureSize)
     */
    public void setFeatureSize(int featureSize) {
        this.featureSize = featureSize;
    }
}
