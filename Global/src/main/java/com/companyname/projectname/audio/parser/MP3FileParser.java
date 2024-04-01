package com.companyname.projectname.audio.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.companyname.projectname.audio.AudioFile;
import com.companyname.projectname.audio.decoder.AudioFrameDecoder;
import com.companyname.projectname.audio.decoder.MP3FrameDecoder;
import com.companyname.projectname.audio.exceptions.AudioFileException;
import com.companyname.projectname.audio.mp3.MP3AudioHeader;
import com.companyname.projectname.audio.mp3.MP3File;

/**
 * MP3 FileParser implementation.
 */
public class MP3FileParser implements FileParser {
	
	public static final String EMPTY_FILE_ARGUMENT_MESSAGE = "Empty argument passed for the fully qualified file name";
	public static final String NO_AUDIO_HEADER_MESSAGE = "No audio header found within ";
	public static final String NO_SUCH_FILE_MESSAGE = "No file found for ";
	public static final String IO_PROBLEM = "IOException occurred whilst trying to parse ";
	
	private static final int FILE_BUFFER_SIZE = 5000;
	private static final int HEADER_SIZE = 4;
	private static final byte[] HEADER = new byte[HEADER_SIZE];
	
	private final Logger logger = LoggerFactory.getLogger(MP3FileParser.class);
	
	@Override
	public AudioFile parse(String fullyQualifiedFileName) throws Exception {

		if (fullyQualifiedFileName == null || fullyQualifiedFileName.length() == 0) {
			throw new AudioFileException(EMPTY_FILE_ARGUMENT_MESSAGE);
		}
		
		fullyQualifiedFileName = fullyQualifiedFileName.trim();
		
		logger.info("Parsing & Decoding File: " + fullyQualifiedFileName);
		
		MP3AudioHeader mp3AudioHeader = new MP3AudioHeader();
		
		// Assumption - The first frame of the audio starts at the first position in the file
		if (!parseMP3File(fullyQualifiedFileName, 0l, mp3AudioHeader)) {
            throw new AudioFileException(NO_AUDIO_HEADER_MESSAGE + fullyQualifiedFileName);
        }
		
		AudioFile mp3File = new MP3File();
        mp3File.setAudioHeader(mp3AudioHeader);
        return mp3File;
	}
	
	private boolean parseMP3File(String fullyQualifiedFileName, long startByte, MP3AudioHeader mp3AudioHeader) {
		
		Path path = Paths.get(fullyQualifiedFileName);
		
		try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path, StandardOpenOption.READ)) {
			
			//Read into Byte Buffer in Chunks
	        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE);
	        
	        // Move Channel to the starting position (skipping over tag if any)
	        seekableByteChannel.position(startByte);
	        
	        seekableByteChannel.read(byteBuffer);
	        byteBuffer.flip();
	        
	        if (MP3FrameDecoder.isMPEGFrame(byteBuffer)) {
	        	logger.info("Found Possible header at: " + startByte);
                mp3AudioHeader.setAudioFrameDecoder(parseMP3Header(byteBuffer));
                // Assumption - All frames in the file are the same, hence only need to parse the first audio frame
            } else {
            	return false;
            }
			
		} catch (NoSuchFileException nse) {
			logger.error(NO_SUCH_FILE_MESSAGE + fullyQualifiedFileName, nse);
			throw new AudioFileException(NO_SUCH_FILE_MESSAGE + fullyQualifiedFileName, nse);
        } catch (IOException iox) {
            logger.error(IO_PROBLEM + fullyQualifiedFileName, iox);
            throw new AudioFileException(IO_PROBLEM + fullyQualifiedFileName, iox);
        } 
		
		MP3File mp3File = new MP3File();
		mp3File.setAudioHeader(mp3AudioHeader);
		return true;
	}
	
    private AudioFrameDecoder parseMP3Header(ByteBuffer bb) throws AudioFileException {
        int position = bb.position();
        bb.get(HEADER, 0, HEADER_SIZE);
        bb.position(position);
        
        AudioFrameDecoder mp3FrameDecoder = new MP3FrameDecoder();
        mp3FrameDecoder.decodeAudioFrameHeader(HEADER);

        return mp3FrameDecoder;
    }
}
