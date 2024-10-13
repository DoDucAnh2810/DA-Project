package cs451.dataType;

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

    @Override
    public String toString() {
        return processId + ":" + sequenceNum; 
    }
}
