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

    public double getFMin() {
        return fMin;
    }

    public void setFMin(double fMin) {
        this.fMin = fMin;
    }

    public double getFMax() {
        return fMax;
    }

    public void setFMax(double fMax) {
        this.fMax = fMax;
    }

    public int getNFft() {
        return nFft;
    }

    public void setNFft(int nFft) {
        this.nFft = nFft;
    }

    public int getHopLength() {
        return hopLength;
    }

    public void setHopLength(int hopLength) {
        this.hopLength = hopLength;
    }

    public int getFeatureSize() {
        return featureSize;
    }

    public void setFeatureSize(int featureSize) {
        this.featureSize = featureSize;
    }
}
