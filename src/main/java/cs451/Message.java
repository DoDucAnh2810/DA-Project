package cs451;

import java.util.ArrayList;
import java.util.List;

public class Message {
    int processId, sequenceNum, length;
    byte[] content;
    
    public Message(int processId, int sequenceNum, byte[] content) {
        this.processId = processId;
        this.sequenceNum = sequenceNum;
        if (content == null)
            this.length = 0;
        else
            this.length = content.length;
        this.content = content;
    }

    public Message(byte[] serialization) {
        for (int i = 3; i >= 0; i--)
            processId = (processId << 8) | serialization[i];
        for (int i = 7; i >= 4; i--)
            sequenceNum = (sequenceNum << 8) | serialization[i];
        for (int i = 11; i >= 8; i--)
            length = (length << 8) | serialization[i];

        content = new byte[length];
        int i = 0, j = 12;
        while (i < length)
            content[i++] = serialization[j++];
    }

    public int processId() {
        return processId;
    }

    public long sequenceNum() {
        return sequenceNum;
    }

    public byte[] content() {
        return content;
    }

    @Override
    public String toString() {
        return processId + ":" + sequenceNum; 
    }

    public byte[] getBytes() {
        int processIdCopy = processId;
        int sequenceNumCopy = sequenceNum;
        int lengthCopy = length;
        byte[] buffer = new byte[12+length];

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
        while (i < 12 + length)
            buffer[i++] = content[j++];

        return buffer;
    }

    public static byte[] groupSerialization(List<Message> messages) {
        int totalLength = 0;
        for (Message message : messages)
            totalLength += 12+message.length;
        byte[] buffer = new byte[totalLength];

        int start = 0;
        for (Message message : messages) {
            int processId = message.processId;
            int sequenceNum = message.sequenceNum;
            int length = message.length;
            int i = 0, j = 0;
            while (i < 4) {
                buffer[start+(i++)] = (byte) (processId & 0xFF);
                processId >>= 8;
            }
            while (i < 8) {
                buffer[start+(i++)] = (byte) (sequenceNum & 0xFF);
                sequenceNum >>= 8;
            }
            while (i < 12) {
                buffer[start+(i++)] = (byte) (length & 0xFF);
                length >>= 8;
            }
            while (i < 12 + message.length)
                buffer[start+(i++)] = message.content[j++];
            start += 12+message.length;
        }

        return buffer;
    }

    public static List<Message> groupDeserialization(byte[] serialization) {
        List<Message> messages = new ArrayList<>();
        int start = 0;
        while (start < serialization.length) {
            int processId = 0, sequenceNum = 0, length = 0;

            for (int i = start+3; i >= start; i--)
                processId = (processId << 8) | serialization[i];
            for (int i = start+7; i >= start+4; i--)
                sequenceNum = (sequenceNum << 8) | serialization[i];
            for (int i = start+11; i >= start+8; i--)
                length = (length << 8) | serialization[i];

            byte[] content = new byte[length];
            int i = 0, j = start+12;
            while (i < length)
                content[i++] = serialization[j++];

            start += 12+length;
            messages.add(new Message(processId, sequenceNum, content));
        }

        return messages;
    }
}
