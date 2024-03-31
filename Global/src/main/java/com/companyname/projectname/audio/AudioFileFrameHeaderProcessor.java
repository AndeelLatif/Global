package com.companyname.projectname.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.companyname.projectname.audio.parser.MP3FileParser;

/**
 * Entry point for processing the audio file for frame header information. 
 * Prints information from the audio file first frame header to the console.
 */
public class AudioFileFrameHeaderProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(AudioFileFrameHeaderProcessor.class);

	public static void main(String[] args) {
        try {
            AudioHeader audioHeader = new MP3FileParser().parse(args[0]).getAudioHeader();
            
            logger.info("Audio Version ID:    " +  audioHeader.getAudioVersion());
            logger.info("Layer:    " +  audioHeader.getLayer());
            logger.info("BitRate:    " +  audioHeader.getBitRateAsNumber());
            logger.info("Sample Rate:    " + audioHeader.getSampleRate());
            logger.info("Channel Mode:   " + audioHeader.getChannels());
            
        } catch (Exception e) {
        	logger.error("Exception occurred whilst trying to find header data from" + args[0]);
        	logger.error(e.getMessage());
        }
	}
}
