package com.companyname.projectname.audio;

public class AudioFile {

    // The Audio header info.
    private AudioHeader audioHeader;

    /**
     * Return audio header information.
     * @return
     */
    public AudioHeader getAudioHeader() {
        return audioHeader;
    }
    
    /**
     * Set audio header information.
     * @param audioHeader
     */
    public void setAudioHeader(AudioHeader audioHeader) {
		this.audioHeader = audioHeader;
	}
}
