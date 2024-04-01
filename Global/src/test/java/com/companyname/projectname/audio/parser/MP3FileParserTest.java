package com.companyname.projectname.audio.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.companyname.projectname.audio.AudioHeader;
import com.companyname.projectname.audio.decoder.MP3FrameDecoder;
import com.companyname.projectname.audio.exceptions.AudioFileException;

@ExtendWith(MockitoExtension.class)
public class MP3FileParserTest {

	@InjectMocks
    private MP3FileParser fileParser;
	
	private String fullyQualifiedTestFile = new File("src/test/resources/LBCNews.mp3").getAbsolutePath();
	
	@Test
	void parse_success() throws Exception {
		AudioHeader audioHeader = fileParser.parse(fullyQualifiedTestFile).getAudioHeader();

		assertEquals("MPEG Version 1", audioHeader.getAudioVersion());
		assertEquals("Layer 3", audioHeader.getLayer());
		assertEquals(256l, audioHeader.getBitRateAsNumber());
		assertEquals("44100", audioHeader.getSampleRate());
		assertEquals("3(Mono)", audioHeader.getChannels());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	void parse_emptyFilePath(String filePath) {
		AudioFileException ex = assertThrows(AudioFileException.class, () -> fileParser.parse(filePath).getAudioHeader());
		assertEquals(MP3FileParser.EMPTY_FILE_ARGUMENT_MESSAGE, ex.getMessage());
	}
	
	@Test
	void parse_isNotMPEGFrame() {
		try (MockedStatic<MP3FrameDecoder> mp3FrameDecoder = Mockito.mockStatic(MP3FrameDecoder.class)) {
			mp3FrameDecoder.when(() -> MP3FrameDecoder.isMPEGFrame(any(ByteBuffer.class)))
	          .thenReturn(false);

			assertThrows(AudioFileException.class, () -> fileParser.parse(fullyQualifiedTestFile).getAudioHeader());
	    }
	}
	
	@Test
	void parse_nonMP3File() throws Exception {
		String fullyQualifiedNonMP3TestFile = new File("src/test/resources/Tunes.txt").getAbsolutePath();
		AudioFileException ex = assertThrows(AudioFileException.class, () -> fileParser.parse(fullyQualifiedNonMP3TestFile).getAudioHeader());
		assertTrue(ex.getMessage().startsWith("No audio header found within"));
	}
	
	@Test
	void parse_nonExistentFile() {
		AudioFileException ex = assertThrows(AudioFileException.class, () -> fileParser.parse("src/test/resources/NonExistent.mp3").getAudioHeader());
		assertTrue(ex.getMessage().startsWith(MP3FileParser.NO_SUCH_FILE_MESSAGE));
	}
	
	// TODO further test cases - will require particular audio files for these:
	// completely empty MP3 file, i.e. no frames - failure test
	// corrupted MP3 file, e.g. missing expected data in the first frame - failure test
	// valid MP3 file but which contains different header data, e.g. stereo file - non copyrighted MP3 file would need to be obtained - success test

}
