package com.companyname.projectname.audio.decoder;

import com.companyname.projectname.audio.exceptions.AudioFileException;

/**
 * Generic Audio Frame Decoder.
 */
public interface AudioFrameDecoder {
	
	/**
     * Try and create a new Audio frame with the given byte array and decodes its contents.
     * If decoding header causes a problem it is not a valid header.
     *
     * @param b the array of bytes representing this audio frame
     * @throws AudioFileException if does not match expected format
     */
	void decodeAudioFrameHeader(byte[] b) throws AudioFileException;
	
	/**
	 * Get the Channel Mode for the decoded Audio frame.
	 * @return the Channel Mode for the decoded Audio frame.
	 */
	String getChannelModeAsString();

	/**
	 * Get the Version for the decoded Audio frame.
	 * @return the Version for the decoded Audio frame.
	 */
    String getVersionAsString();

    /**
	 * Get the Bit Rate for the decoded Audio frame.
	 * @return the Bit Rate for the decoded Audio frame.
	 */
    Integer getBitRate();

    /**
	 * Get the Sampling Rate for the decoded Audio frame.
	 * @return the Sampling Rate for the decoded Audio frame.
	 */
    Integer getSamplingRate();

    /**
	 * Get the Layer for the decoded Audio frame.
	 * @return the Layer for the decoded Audio frame.
	 */
    String getLayerAsString();

}
