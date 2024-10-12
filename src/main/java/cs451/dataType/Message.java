package cs451.dataType;

public class Message {
    short processId;
    long sequenceNum;
    byte[] content;
    
    public Message(short processId, long sequenceNum, byte[] content) {
        this.processId = processId;
        this.sequenceNum = sequenceNum;
        this.content = content;
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
