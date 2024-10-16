package cs451.communication;

/**
 * Message
 */
public class Message {
    public static final byte PerfectAck = 0b00010000;
    public static final byte GroupedMes = 0b00000001;
    public static final byte FIFOPstMes = 0b00000010;
    private static final byte AckMask = 0x70;
    private static final byte HSfMask = 0x0F;
    private int processId, sequenceNum, length;
    private byte type;
    private byte[] content;
    private String hash;
    

    public Message(int processId, int sequenceNum, byte[] content) {
        this(processId, sequenceNum, content, (byte)0);
    }


    public Message(Message message, byte ack) {
        this(message.processId, message.sequenceNum, message.content, 
             (byte)(message.type | (ack & AckMask)));
    } 


    public Message(int processId, int sequenceNum, byte[] content, byte type) {
        this.processId = processId;
        this.sequenceNum = sequenceNum;
        if (content == null)
            this.length = 0;
        else
            this.length = content.length;
        this.content = content;
        this.type = type;
        this.hash = calculateHash(); 
    }


    public Message(byte[] serialization) {
        processId = 0;
        sequenceNum = 0;
        length = 0;
        for (int i = 3; i >= 0; i--)
            processId = (processId << 8) | (serialization[i] & 0xFF);
        for (int i = 7; i >= 4; i--)
            sequenceNum = (sequenceNum << 8) | (serialization[i] & 0xFF);
        for (int i = 11; i >= 8; i--)
            length = (length << 8) | (serialization[i] & 0xFF);
        type = serialization[12];
        content = new byte[length];
        int i = 0, j = 13;
        while (i < length)
            content[i++] = serialization[j++];
        hash = calculateHash();
    }


    private String calculateHash() {
        return processId + ":" + sequenceNum + ":" + (type & HSfMask);
    }


    public int processId() {
        return processId;
    }


    public int sequenceNum() {
        return sequenceNum;
    }


    public int length() {
        return length;
    }


    public byte[] content() {
        return content;
    }


    public byte type() {
        return type;
    }


    public String hash() {
        return hash;
    }


    public boolean isPerfectAck() {
        return (type & PerfectAck) != 0;
    }
    

    public byte[] getBytes() {
        int processIdCopy = processId;
        int sequenceNumCopy = sequenceNum;
        int lengthCopy = length;
        byte[] buffer = new byte[13+length];

        int i = 0, j = 0;
        while (i < 4) {
            buffer[i++] = (byte) (processIdCopy & 0xFF);
            processIdCopy >>= 8;
        }
        while (i < 8) {
            buffer[i++] = (byte) (sequenceNumCopy & 0xFF);
            sequenceNumCopy >>= 8;
        }
        while (i < 12) {
            buffer[i++] = (byte) (lengthCopy & 0xFF);
            lengthCopy >>= 8;
        }
        buffer[i++] = type;
        while (i < 13 + length)
            buffer[i++] = content[j++];

        return buffer;
    }
}
