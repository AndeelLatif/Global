package com.companyname.projectname.audio.mp3;

import com.companyname.projectname.audio.AudioHeader;
import com.companyname.projectname.audio.decoder.AudioFrameDecoder;

/**
 * Represents the audio header of an MP3 File
 */
public class MP3AudioHeader implements AudioHeader {

	private static final String FILE_FORMAT = "Mp3";
	
	private AudioFrameDecoder mp3FrameDecoder;

	@Override
	public String getEncodingType() {
		return mp3FrameDecoder.getVersionAsString() + " " + mp3FrameDecoder.getLayerAsString();
	}

	@Override
	public String getBitRate() {
        return String.valueOf(mp3FrameDecoder.getBitRate());
	}

	@Override
	public long getBitRateAsNumber() {
		return mp3FrameDecoder.getBitRate();
	}

	@Override
	public String getSampleRate() {
		return String.valueOf(mp3FrameDecoder.getSamplingRate());
	}

	@Override
	public int getSampleRateAsNumber() {
		return mp3FrameDecoder.getSamplingRate();
	}

	@Override
	public String getFormat() {
		return FILE_FORMAT;
	}

	@Override
	public String getChannels() {
		return mp3FrameDecoder.getChannelModeAsString();
	}
	
    @Override
    public String getAudioVersion() {
    	return mp3FrameDecoder.getVersionAsString();
    }
    
    @Override
    public String getLayer() {
    	return mp3FrameDecoder.getLayerAsString();
    }
    
    @Override
    public AudioFrameDecoder getAudioFrameDecoder() {
		return mp3FrameDecoder;
	}

    @Override
    public void setAudioFrameDecoder(AudioFrameDecoder mp3FrameHeader) {
		this.mp3FrameDecoder = mp3FrameHeader;
	}
}
