package com.companyname.projectname.audio.mp3;

import com.companyname.projectname.audio.AudioFile;

/**
 * This class represents the information of interest contained in an MP3 File.
 */
public class MP3File extends AudioFile {

    /**
     * Return audio header
     * @return
     */
    public MP3AudioHeader getMP3AudioHeader() {
        return (MP3AudioHeader) getAudioHeader();
    }
}
