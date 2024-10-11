package cs451;

public class Message {
    short srcId;
    long seqNum;
    byte[] content;
    String hash;
    
    public Message(short srcId, long seqNum, byte[] content) {
        this.srcId = srcId;
        this.seqNum = seqNum;
        this.content = content;
    }

    public byte[] getBytes() {
        short srcIdCopy = srcId;
        long seqNumCopy = seqNum;
        byte[] buffer = new byte[32];

        int i = 0, j = 0;
        while (i < 2) {
            buffer[i++] = (byte) (srcIdCopy & 0xFF);
            srcIdCopy >>= 8;
        }
        while (i < 10) {
            buffer[i++] = (byte) (seqNumCopy & 0xFF);
            seqNumCopy >>= 8;
        }
        if (content != null)
            while (i < 32 && j < content.length)
                buffer[i++] = content[j++];

        return buffer;
    }

    @Override
    public String toString() {
        return srcId + ":" + seqNum; 
    }
}
