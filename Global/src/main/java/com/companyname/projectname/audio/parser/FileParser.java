package com.companyname.projectname.audio.parser;

import com.companyname.projectname.audio.AudioFile;

public interface FileParser {

	/**
	 * Parses an audio file to obtain information contained in the file.
	 * @param fullyQualifiedFileName
	 * @return
	 * @throws Exception
	 */
	AudioFile parse(String fullyQualifiedFileName) throws Exception;

}
