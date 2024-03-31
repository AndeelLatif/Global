package com.companyname.projectname.audio.exceptions;

/**
 * Exception class for handling and processing an audio file.
 */
public class AudioFileException extends RuntimeException {

	private static final long serialVersionUID = 8955998423442996476L;

	public AudioFileException() {
	}

	public AudioFileException(String message) {
		super(message);
	}

	public AudioFileException(Throwable cause) {
		super(cause);
	}

	public AudioFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public AudioFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
