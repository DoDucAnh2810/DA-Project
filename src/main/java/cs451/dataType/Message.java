package cs451.dataType;

import cs451.Constants;

public class Message {
    short processId;
    long sequenceNum;
    byte[] content;
    
    public Message(short processId, long sequenceNum, byte[] content) {
        this.processId = processId;
        this.sequenceNum = sequenceNum;
        this.content = content;
    }

    public Message(byte[] serialization) {
        processId = (short)((serialization[1] << 8) | serialization[0]);
        
        sequenceNum = 0;
        for (int i = 9; i >= 2; i--)
            sequenceNum = (sequenceNum << 8) | serialization[i];

        content = new byte[Constants.BYTE_OF_CONTENT];
        int m = 0;
        int n = 10;
        while (m < Constants.BYTE_OF_CONTENT)
            content[m++] = serialization[n++];
    }

    public byte[] getBytes() {
        short processIdCopy = processId;
        long sequenceNumCopy = sequenceNum;
        byte[] buffer = new byte[32];

        int i = 0, j = 0;
        while (i < 2) {
            buffer[i++] = (byte) (processIdCopy & 0xFF);
            processIdCopy >>= 8;
        }
        while (i < 10) {
            buffer[i++] = (byte) (sequenceNumCopy & 0xFF);
            sequenceNumCopy >>= 8;
        }
        if (content != null)
            while (i < 32 && j < content.length)
                buffer[i++] = content[j++];

        return buffer;
    }

    @Override
    public String toString() {
        return processId + ":" + sequenceNum; 
    }
}
