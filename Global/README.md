
# MP3 Audio Frame Contents Reader Program

## Program Description:

Reads the first audio frame from an MP3 file and prints the following information about the file to console:
1. the MPEG Audio Version ID of the file
2. the MPEG Layer of the file
3. the bitrate of the file
4. the sample rate of the file
5. the channel mode of the file

## Assumptions:

• the first frame of the audio starts at the first position in the file (i.e. there is nothing extra in the file other than the audio).

• the audio was encoded with an encoder built to this spec: http://www.mp3-tech.org/programmer/frame_header.html.

• all frames in the file are the same.



## Implementation Notes:

THIS PROGRAM HAS BEEN IMPLEMENTED AS A PROTOTYPE. IN IT'S CURRENT FORM THIS IS NOT TO BE RELEASED INTO ANY PRODUCTION ENVIRONMENT AND IS NOT INTENDED FOR COMMERCIAL USE.

• Implemented as a Java 18 out-of-container/stand-alone command line utility with a class containing a 'main(String[] args)' method.

• Service State - The current implementation is stand-alone and does not maintain any state between runs; there is no database or file store.

• Authorisation & Authentication & Encryption - No provision is made for these. This can be implemented in a future iteration once requirements for these are published



## Pre-Requisities:

• Java 18 JRE (for running the program)

• Java 18 JDK (for development purposes)

• Maven - any recent version



## Instructions For Testing:

Run the JUnits. They are stand-alone - they do not require any further setup.



## Instructions For Packaging & Use:

There are two ways of using this program to produce results to the command line/console:

A. Run the com.companyname.projectname.audio.AudioFileFrameHeaderProcessor.java class directly within an IDE passing in a valid fully qualified file path and file name.
	e.g. C:\\Projects\\Global\\src\\test\\resources\\LBCNews.mp3
	
B. Package the program up using Maven and run from the command line passing in a valid fully qualified file path and file name. e.g.

	maven clean package
	java -jar Global-1.0.0.jar C:\\Projects\\LBC\\Global\\src\\test\\resources\\LBCNews.mp3
	

Note, an uber jar will be created using the Maven Shade Plugin

In addition the resulting jar file can be imported as a library or dependencey and it's functionality can be invoked as per the examples in the JUnit test contained therein.
 
 
 
## Possible Improvements:

•	This program has been developed to be run as a stand-alone library. It could in theory be run as a service (e.g. Microservice over HTTP) but according to current functionality there should really be no need for this.

•	Spring dependency injection may be used to replace the use of constructors where appropriate. This may benefit JUnit testing.

•	Third party libraries may be used for the parsing and decoding, but the use of thier party libraries was not in scope for the original implementation.

•	Further JUnit tests are required - refer to TODO notes in com.companyname.projectname.audio.parser.MP3FileParserTest.java + JUnit test required for com.companyname.projectname.audio.decoder.MP3FrameDecoder.java

•	com.companyname.projectname.audio.mp3.MP3File.java might benefit from overriding equals() & hashCode() & toString() methods and implementing Comparable<T> & Serializable interfaces depending on future use.

•	com.companyname.projectname.audio.mp3.MP3File.java might benefit from Lombok annotations, e.g. @NoArgsConstructor, @AllArgsConstructor, @Builder

•	Further audio file types can be added, e.g. wav but implementing the following interfaces:
		com.companyname.projectname.audio.AudioFile.java
		com.companyname.projectname.audio.AudioHeader.java
		com.companyname.projectname.audio.decoder.AudioFrameDecoder.java
		com.companyname.projectname.audio.parser.FileParser.java
		
Note, the capablities of these interfaces may need to be enhanced as per future requirements.


