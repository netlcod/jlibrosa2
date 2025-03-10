package org.netlcod.feature;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FeatureTest {
    private static final double EPS = 1e-4;
    private FeatureConfig featureConfig;
    private double[] inputData;
    private double[][] etalonMelData;
    private double[][] etalonMfccData;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File configFile = new File("src/test/resources/feature_configuration.json");
        featureConfig = objectMapper.readValue(configFile, FeatureConfig.class);

        File inputFile = new File("src/test/resources/input_data.json");
        inputData = objectMapper.readValue(inputFile, double[].class);

        File etalonMelFile = new File("src/test/resources/mel_etalon.json");
        etalonMelData = objectMapper.readValue(etalonMelFile, double[][].class);

        File etalonMfccFile = new File("src/test/resources/mfcc_etalon.json");
        etalonMfccData = objectMapper.readValue(etalonMfccFile, double[][].class);
    }

    @Test
    public void testMel() {
        MelFeature mel_extractor = new MelFeature();
        mel_extractor.setSampleRate(featureConfig.sampleRate);
        mel_extractor.setNFft(featureConfig.nFft);
        mel_extractor.setHopLength(featureConfig.hopLength);
        mel_extractor.setFeatureSize(featureConfig.nMels);

        double[][] result = mel_extractor.extract(inputData);

        for (int i = 0; i < result.length; i++) {
            assertArrayEquals(result[i], etalonMelData[i], EPS, "Row " + i + " does not match");
        }
    }

    @Test
    public void testMfcc() {
        MfccFeature mfcc_extractor = new MfccFeature();
        mfcc_extractor.setSampleRate(featureConfig.sampleRate);
        mfcc_extractor.setNFft(featureConfig.nFft);
        mfcc_extractor.setHopLength(featureConfig.hopLength);
        mfcc_extractor.setFeatureSize(featureConfig.nMfcc);

        double[][] result = mfcc_extractor.extract(inputData);

        for (int i = 0; i < result.length; i++) {
            assertArrayEquals(result[i], etalonMfccData[i], EPS, "Row " + i + " does not match");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class FeatureConfig {
        public int sampleRate;
        public int nFft;
        public int hopLength;
        public int nMels;
        public int nMfcc;
    }
}
