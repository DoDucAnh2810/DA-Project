package cs451.communication;

import java.util.List;
import java.util.ArrayList;

/**
 * Message
 */
public class Message {
    public static final byte PerfectAck = 0b00010000;
    public static final byte GroupedMes = 0b00000001;
    public static final byte FIFOPstMes = 0b00000010;
    private static final byte AcksMask = 0x70;
    private static final byte HashMask = 0x0F;

    private int processId, sequenceNum, length;
    private byte[] content;
    private byte type;
    private String hash;
    private int srcId;
    

    public Message(int processId, int sequenceNum, byte[] content) {
        this(processId, sequenceNum, content, (byte)0, processId);
    }


    public Message(int processId, int sequenceNum, byte[] content, byte type) {
        this(processId, sequenceNum, content, type, processId);
    }


    public Message(int processId, int sequenceNum, byte[] content, byte type, int srcId) {
        this.processId = processId;
        this.sequenceNum = sequenceNum;
        if (content == null)
            this.length = 0;
        else
            this.length = content.length;
        this.content = content;
        this.type = type;
        this.hash = calculateHash();
        this.srcId = srcId;
    }


    public Message(Message message, byte ack) {
        this(message.processId, message.sequenceNum, message.content, 
             (byte)(message.type | (ack & AcksMask)), message.srcId);
    } 


    public Message(byte[] serialization) {
        Message message = deserialization(serialization, 0);
        this.processId = message.processId;
        this.sequenceNum = message.sequenceNum;
        this.length = message.length;
        this.content = message.content;
        this.type = message.type;
        this.hash = message.hash;
        this.srcId = message.srcId;
    }


    private String calculateHash() {
        return processId + ":" + sequenceNum + ":" + (type & HashMask);
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


    public int srcId() {
        return srcId;
    }
    

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public boolean isPerfectAck() {
        return (type & PerfectAck) != 0;
    }


    public static int byteSize(Message message) {
        return 13 + message.length;
    }


    public static int groupByteSize(List<Message> messages) {
        int accum = 0;
        for (Message message : messages)
            accum += byteSize(message);
        return accum;
    }
    

    public byte[] getBytes() {
        byte[] buffer = new byte[byteSize(this)];
        serialization(this, buffer, 0);
        return buffer;
    }


    private static void serialization(Message message, byte[] buffer, int start) {
        int srcIdCopy = message.srcId;
        int processIdCopy = message.processId;
        int sequenceNumCopy = message.sequenceNum;
        int lengthCopy = message.length;
        int i = 0, j = 0;

        while (i < 2) {
            buffer[start+(i++)] = (byte) (srcIdCopy & 0xFF);
            srcIdCopy >>= 8;
        }
        while (i < 4) {
            buffer[start+(i++)] = (byte) (processIdCopy & 0xFF);
            processIdCopy >>= 8;
        }
        while (i < 8) {
            buffer[start+(i++)] = (byte) (sequenceNumCopy & 0xFF);
            sequenceNumCopy >>= 8;
        }
        while (i < 12) {
            buffer[start+(i++)] = (byte) (lengthCopy & 0xFF);
            lengthCopy >>= 8;
        }
        buffer[start+(i++)] = message.type;
        while (i < 13 + message.length)
            buffer[start+(i++)] = message.content[j++];
    }


    private static Message deserialization(byte[] serialization, int start) {
        int srcId = 0, processId = 0, sequenceNum = 0, length = 0, i, j;
        for (i = 1; i >= 0; i--)
            srcId = (srcId << 8) | (serialization[start+i] & 0xFF);
        for (i = 3; i >= 2; i--)
            processId = (processId << 8) | (serialization[start+i] & 0xFF);
        for (i = 7; i >= 4; i--)
            sequenceNum = (sequenceNum << 8) | (serialization[start+i] & 0xFF);
        for (i = 11; i >= 8; i--)
            length = (length << 8) | (serialization[start+i] & 0xFF);
        byte type = serialization[start+12];
        byte[] content = new byte[length];
        i = 0; j = 13;
        while (i < length)
            content[i++] = serialization[start+(j++)];
        return new Message(processId, sequenceNum, content, type, srcId);
    }


    public static byte[] groupSerialization(List<Message> messages) {
        byte[] buffer = new byte[groupByteSize(messages)];
        int start = 0;
        for (Message message : messages) {
            serialization(message, buffer, start);
            start += byteSize(message);
        }
        return buffer;
    }

    
    public static List<Message> groupDeserialization(byte[] serialization) {
        List<Message> messages = new ArrayList<Message>();
        int start = 0;
        while (start < serialization.length) {
            Message message = deserialization(serialization, start);
            messages.add(message);
            start += byteSize(message);
        }
        return messages;
    }
}
