package com.companyname.projectname.audio;

import com.companyname.projectname.audio.decoder.AudioFrameDecoder;

/**
 * Contains info about the Audio Header.
 */
public interface AudioHeader {

	/**
     * @return the audio file type
     */
    String getEncodingType();

    /**
     * @return the BitRate of the Audio, this is the amount of kilobits of data sampled per second
     */
    String getBitRate();

    /**
     * @return bitRate as a number, this is the amount of kilobits of data sampled per second
     */
    long getBitRateAsNumber();

    /**
     * @return the Sampling rate, the number of samples taken per second
     */
    String getSampleRate();

    /**
     * @return he Sampling rate, the number of samples taken per second
     */
    int getSampleRateAsNumber();

    /**
     * @return the format
     */
    String getFormat();

    /**
     * @return the number of channels (i.e 1 = Mono, 2 = Stereo)
     */
    String getChannels();
    
    /**
     * @return audio version, e.g. MPEG Version (1-3)
     */
    String getAudioVersion();
    
    /**
     * @return layer, e.g. for MPEG 'Layer (1-3)'
     */
    String getLayer();
    
    /**
     * @return AudioFrameDecoder for this AudioHeader instance. 
     */
    AudioFrameDecoder getAudioFrameDecoder();
    
    /**
     * Sets the AudioFrameDecoder for this AudioHeader instance.
     * @param audioFrameHeader
     */
    void setAudioFrameDecoder(AudioFrameDecoder audioFrameHeader);
}
