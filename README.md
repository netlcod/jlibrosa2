
# jlibrosa2

A library for extracting audio signal features in accordance with the [librosa](https://github.com/librosa/librosa)

---

## Features

- STFT / Inverse STFT

- Mel-Spectrogram / MFCC

- Window Functions: bartlett, blackman, hamming, hann, welch

- Time/Frequency Conversion

---

## Requirements

- Java Development Kit (JDK) 8 or higher
- Apache Commons Math library (org.apache.commons.math3)

## Example

**STFT / Inverse STFT**
```java
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
```

**Feature extraction**
```java
// Mel
MelFeature melFeature = new MelFeature();
melFeature.setSampleRate(16000);
melFeature.setNFft(1024);
melFeature.setHopLength(512);
melFeature.setFeatureSize(128);

// MFCC
MfccFeature mfccFeature = new MfccFeature();
mfccFeature.setSampleRate(16000);
mfccFeature.setNFft(1024);
mfccFeature.setHopLength(512);
mfccFeature.setFeatureSize(40);

double[] y = ...; // Input audio signal
double[][] melSpectrogram = melFeature.extract(y);
double[][] mfcc = mfccFeature.extract(y);
```

**Windowing**
```java
double[] window = Window.bartlett(winLength, true);
for (int i=0; i<y.length; i++) {
    int winIndex = i % winLength;
    windowedSignal[i] = y[i] * window[winIndex];
}
```

## Project structure

```
jlibrosa2/
├── src/
│   ├── main/
│   │   ├── java/org/netlcod/convert/
│   │   │   ├── Convert.java                       # Time-frequency conversion utilities
│   │   ├── java/org/netlcod/feature/
│   │   │   ├── AudioFeatureConfiguration.java     # Base configuration for audio feature extraction
│   │   │   ├── MelFeature.java                    # Mel-spectrogram computation
│   │   │   ├── MfccFeature.java                   # MFCC computation
│   │   ├── java/org/netlcod/filter/
│   │   │   ├── Filter.java                        # Mel filter bank generation and application
│   │   ├── java/org/netlcod/spectrum/
│   │   │   ├── Spectrum.java                      # Spectrum analysis (STFT, power/amplitude to dB conversions)
│   │   ├── java/org/netlcod/window/
│   │   │   ├── Window.java                        # Windowing functions
│   ├── resources/
│   │   ├── README.md                              # Project documentation
├── build.gradle                                   # Gradle build configuration
```

## License

This project is licensed under the GNU Lesser General Public License v3.0. See the [LICENSE](LICENSE) file for details.