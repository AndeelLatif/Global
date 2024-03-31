package com.companyname.projectname.audio.decoder;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.companyname.projectname.audio.exceptions.AudioFileException;

/**
 * An MP3 is made up of a number of frames each frame starts with a four byte frame header.
 * Assumption - the audio was encoded with an encoder built to this spec: 
 * http://www.mp3-tech.org/programmer/frame_header.html
 */
public class MP3FrameDecoder implements AudioFrameDecoder{
	
	// Definitions of the bit used when reading file format from file
	private static final int BIT7 = 0x80;
	private static final int BIT6 = 0x40;
	private static final int BIT5 = 0x20;
	private static final int BIT4 = 0x10;
	private static final int BIT3 = 0x08;
	private static final int BIT2 = 0x04;
	private static final int BIT1 = 0x02;

    // Constants for MP3 Frame header, each frame has a basic header of 4 bytes
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;

    private static final int SYNC_BYTE1 = 0xFF;
    private static final int SYNC_BYTE2 = 0xE0;
    private static final int SYNC_BIT_ANDSAMPING_BYTE3 = 0xFC;

    // Constants for MPEG Version
    private static final Map<Integer, String> mpegVersionMap = new HashMap<Integer, String>();
    private final static int VERSION_2_5 = 0;
    private final static int VERSION_2 = 2;
    private final static int VERSION_1 = 3;

    static {
        mpegVersionMap.put(VERSION_2_5, "MPEG Version 2.5");
        mpegVersionMap.put(VERSION_2, "MPEG Version 2");
        mpegVersionMap.put(VERSION_1, "MPEG Version 1");
    }

    
    // Constants for MPEG Layer
    private static final Map<Integer, String> mpegLayerMap = new HashMap<Integer, String>();
    private final static int LAYER_I = 3;
    private final static int LAYER_II = 2;
    private final static int LAYER_III = 1;

    static {
        mpegLayerMap.put(LAYER_I, "Layer 1");
        mpegLayerMap.put(LAYER_II, "Layer 2");
        mpegLayerMap.put(LAYER_III, "Layer 3");
    }

    // Bit Rates, the setBitrate varies for different Version and Layer
    private static final Map<Integer, Integer> bitrateMap = new HashMap<Integer, Integer>();

    static
    {
        // MPEG-1, Layer I (E)
        bitrateMap.put(0x1E, 32);
        bitrateMap.put(0x2E, 64);
        bitrateMap.put(0x3E, 96);
        bitrateMap.put(0x4E, 128);
        bitrateMap.put(0x5E, 160);
        bitrateMap.put(0x6E, 192);
        bitrateMap.put(0x7E, 224);
        bitrateMap.put(0x8E, 256);
        bitrateMap.put(0x9E, 288);
        bitrateMap.put(0xAE, 320);
        bitrateMap.put(0xBE, 352);
        bitrateMap.put(0xCE, 384);
        bitrateMap.put(0xDE, 416);
        bitrateMap.put(0xEE, 448);
        
        // MPEG-1, Layer II (C)
        bitrateMap.put(0x1C, 32);
        bitrateMap.put(0x2C, 48);
        bitrateMap.put(0x3C, 56);
        bitrateMap.put(0x4C, 64);
        bitrateMap.put(0x5C, 80);
        bitrateMap.put(0x6C, 96);
        bitrateMap.put(0x7C, 112);
        bitrateMap.put(0x8C, 128);
        bitrateMap.put(0x9C, 160);
        bitrateMap.put(0xAC, 192);
        bitrateMap.put(0xBC, 224);
        bitrateMap.put(0xCC, 256);
        bitrateMap.put(0xDC, 320);
        bitrateMap.put(0xEC, 384);
        
        // MPEG-1, Layer III (A)
        bitrateMap.put(0x1A, 32);
        bitrateMap.put(0x2A, 40);
        bitrateMap.put(0x3A, 48);
        bitrateMap.put(0x4A, 56);
        bitrateMap.put(0x5A, 64);
        bitrateMap.put(0x6A, 80);
        bitrateMap.put(0x7A, 96);
        bitrateMap.put(0x8A, 112);
        bitrateMap.put(0x9A, 128);
        bitrateMap.put(0xAA, 160);
        bitrateMap.put(0xBA, 192);
        bitrateMap.put(0xCA, 224);
        bitrateMap.put(0xDA, 256);
        bitrateMap.put(0xEA, 320);
        
        // MPEG-2, Layer I (6)
        bitrateMap.put(0x16, 32);
        bitrateMap.put(0x26, 48);
        bitrateMap.put(0x36, 56);
        bitrateMap.put(0x46, 64);
        bitrateMap.put(0x56, 80);
        bitrateMap.put(0x66, 96);
        bitrateMap.put(0x76, 112);
        bitrateMap.put(0x86, 128);
        bitrateMap.put(0x96, 144);
        bitrateMap.put(0xA6, 160);
        bitrateMap.put(0xB6, 176);
        bitrateMap.put(0xC6, 192);
        bitrateMap.put(0xD6, 224);
        bitrateMap.put(0xE6, 256);
        
        // MPEG-2, Layer II (4)
        bitrateMap.put(0x14, 8);
        bitrateMap.put(0x24, 16);
        bitrateMap.put(0x34, 24);
        bitrateMap.put(0x44, 32);
        bitrateMap.put(0x54, 40);
        bitrateMap.put(0x64, 48);
        bitrateMap.put(0x74, 56);
        bitrateMap.put(0x84, 64);
        bitrateMap.put(0x94, 80);
        bitrateMap.put(0xA4, 96);
        bitrateMap.put(0xB4, 112);
        bitrateMap.put(0xC4, 128);
        bitrateMap.put(0xD4, 144);
        bitrateMap.put(0xE4, 160);
        
        // MPEG-2, Layer III (2)
        bitrateMap.put(0x12, 8);
        bitrateMap.put(0x22, 16);
        bitrateMap.put(0x32, 24);
        bitrateMap.put(0x42, 32);
        bitrateMap.put(0x52, 40);
        bitrateMap.put(0x62, 48);
        bitrateMap.put(0x72, 56);
        bitrateMap.put(0x82, 64);
        bitrateMap.put(0x92, 80);
        bitrateMap.put(0xA2, 96);
        bitrateMap.put(0xB2, 112);
        bitrateMap.put(0xC2, 128);
        bitrateMap.put(0xD2, 144);
        bitrateMap.put(0xE2, 160);
    }

    // Constants for Channel mode
    private static final Map<Integer, String> modeMap = new HashMap<Integer, String>();
    private final static int MODE_STEREO = 0;
    private final static int MODE_JOINT_STEREO = 1;
    private final static int MODE_DUAL_CHANNEL = 2;
    private final static int MODE_MONO = 3;

    static {
        modeMap.put(MODE_STEREO, "Stereo");
        modeMap.put(MODE_JOINT_STEREO, "Joint Stereo");
        modeMap.put(MODE_DUAL_CHANNEL, "Dual");
        modeMap.put(MODE_MONO, "Mono");
    }

    // Sampling Rate in Hz
    private static final Map<Integer, Map<Integer, Integer>> samplingRateMap = new HashMap<Integer, Map<Integer, Integer>>();
    private static final Map<Integer, Integer> samplingV1Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplingV2Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplingV25Map = new HashMap<Integer, Integer>();

    static {
        samplingV1Map.put(0, 44100);
        samplingV1Map.put(1, 48000);
        samplingV1Map.put(2, 32000);

        samplingV2Map.put(0, 22050);
        samplingV2Map.put(1, 24000);
        samplingV2Map.put(2, 16000);

        samplingV25Map.put(0, 11025);
        samplingV25Map.put(1, 12000);
        samplingV25Map.put(2, 8000);

        samplingRateMap.put(VERSION_1, samplingV1Map);
        samplingRateMap.put(VERSION_2, samplingV2Map);
        samplingRateMap.put(VERSION_2_5, samplingV25Map);
    }

    // Samples Per Frame
    private static final Map<Integer, Map<Integer, Integer>> samplesPerFrameMap = new HashMap<Integer, Map<Integer, Integer>>();
    private static final Map<Integer, Integer> samplesPerFrameV1Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplesPerFrameV2Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplesPerFrameV25Map = new HashMap<Integer, Integer>();

    static {
        samplesPerFrameV1Map.put(LAYER_I, 384);
        samplesPerFrameV1Map.put(LAYER_II, 1152);
        samplesPerFrameV1Map.put(LAYER_III, 1152);

        samplesPerFrameV2Map.put(LAYER_I, 384);
        samplesPerFrameV2Map.put(LAYER_II, 1152);
        samplesPerFrameV2Map.put(LAYER_III, 1152);

        samplesPerFrameV25Map.put(LAYER_I, 384);
        samplesPerFrameV25Map.put(LAYER_II, 1152);
        samplesPerFrameV25Map.put(LAYER_III, 1152);

        samplesPerFrameMap.put(VERSION_1, samplesPerFrameV1Map);
        samplesPerFrameMap.put(VERSION_2, samplesPerFrameV2Map);
        samplesPerFrameMap.put(VERSION_2_5, samplesPerFrameV25Map);
    }

    // MP3 Frame Header bit mask
    private static final int MASK_MP3_ID = BIT3;

    // MP3 version, confusingly for MP3s the version is 1.
    private static final int MASK_MP3_VERSION = BIT4 | BIT3;

    // MP3 Layer, for MP3s the Layer is 3
    private static final int MASK_MP3_LAYER = BIT2 | BIT1;

    // The setBitrate of this MP3
    private static final int MASK_MP3_BITRATE = BIT7 | BIT6 | BIT5 | BIT4;

    // The sampling/frequency rate
    private static final int MASK_MP3_FREQUENCY = BIT3 + BIT2;

    // Channel Mode, Stero/Mono/Dual Channel
    private static final int MASK_MP3_MODE = BIT7 | BIT6;

    private byte[] mpegBytes;

    // The version of this MPEG frame (see the constants)
    private int version;

    private String versionAsString;

    // Contains the mpeg layer of this frame (see constants)
    private int layer;

    private String layerAsString;
    
    // Bitrate of this frame
    private Integer bitRate;

    // Channel Mode of this Frame (see constants)
    private int channelMode;

    // Channel Mode of this Frame As English String
    private String channelModeAsString;

    private Integer samplingRate;
    
    /**
     * Try and create a new MPEG frame with the given byte array and decodes its contents
     * If decoding header causes a problem it is not a valid header
     *
     * @param b the array of bytes representing this mpeg frame
     * @throws AudioFileException if does not match expected format
     */
    @Override
    public void decodeAudioFrameHeader(byte[] b) throws AudioFileException {
        mpegBytes = b;
        setBitrate();
        setVersion();
        setLayer();
        setSamplingRate();
        setChannelMode();
    }

    @Override
    public String getChannelModeAsString() {
        return channelModeAsString;
    }

    @Override
    public String getVersionAsString() {
        return versionAsString;
    }

    @Override
    public Integer getBitRate() {
        return bitRate;
    }

    @Override
    public Integer getSamplingRate() {
        return samplingRate;
    }

    @Override
    public String getLayerAsString() {
        return layerAsString;
    }
    
    /**
     * Gets the MPEGFrame attribute of the MPEGFrame object
     *
     * @param bb
     * @return The mPEGFrame value
     */
    public static boolean isMPEGFrame(ByteBuffer bb) {
        int position = bb.position();
        return (((bb.get(position) & SYNC_BYTE1) == SYNC_BYTE1)
                && ((bb.get(position + 1) & SYNC_BYTE2) == SYNC_BYTE2)
                && ((bb.get(position + 2) & SYNC_BIT_ANDSAMPING_BYTE3) != SYNC_BIT_ANDSAMPING_BYTE3));
    }

    private void setVersion() throws AudioFileException {
        version = (byte) ((mpegBytes[BYTE_2] & MASK_MP3_VERSION) >> 3);
        versionAsString = mpegVersionMap.get(version);

        if (versionAsString == null) {
            throw new AudioFileException("Invalid mpeg version");
        }
    }

    private void setBitrate() throws AudioFileException {
        // BitRate, get by checking header setBitrate bits and MPEG Version and Layer
        int bitRateIndex = mpegBytes[BYTE_3] & MASK_MP3_BITRATE | mpegBytes[BYTE_2] & MASK_MP3_ID | mpegBytes[BYTE_2] & MASK_MP3_LAYER;

        bitRate = bitrateMap.get(bitRateIndex);
        if (bitRate == null)
        {
            throw new AudioFileException("Invalid bitrate");
        }
    }

    private void setChannelMode() throws AudioFileException {
        channelMode = (mpegBytes[BYTE_4] & MASK_MP3_MODE) >>> 6;
        channelModeAsString = channelMode + "(" + modeMap.get(channelMode) + ")";
        
        if (channelModeAsString == null) {
            throw new AudioFileException("Invalid channel mode");
        }
    }

    private void setLayer() throws AudioFileException {
        layer = (mpegBytes[BYTE_2] & MASK_MP3_LAYER) >>> 1;
        layerAsString = mpegLayerMap.get(layer);
        if (layerAsString == null) {
            throw new AudioFileException("Invalid Layer");
        }
    }

    private void setSamplingRate() throws AudioFileException {
        int index = (mpegBytes[BYTE_3] & MASK_MP3_FREQUENCY) >>> 2;
        Map<Integer, Integer> samplingRateMapForVersion = samplingRateMap.get(version);
        
        if (samplingRateMapForVersion == null) {
            throw new AudioFileException("Invalid version");
        }
        
        samplingRate = samplingRateMapForVersion.get(index);
        
        if (samplingRate == null) {
            throw new AudioFileException("Invalid sampling rate");
        }
    }

}
