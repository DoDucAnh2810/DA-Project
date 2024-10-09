package cs451;

import java.util.Arrays;

public class Message {
    short srcId;
    long seqNum;
    byte[] content;

    int hash;
    
    Message(short srcId, long seqNum, byte[] content) {
        this.srcId = srcId;
        this.seqNum = seqNum;
        this.content = content;
        this.hash = generateHash();
    }

    Message(byte[] buffer) {

    }

    byte[] getBytes() {
        short srcIdCopy = srcId;
        long seqNumCopy = seqNum;
        byte[] buffer = new byte[138]; // 2+8+128

        int i = 0, j = 0;
        while (i < 2) {
            buffer[i++] = (byte) (srcIdCopy & 0xFF);
            srcIdCopy >>= 8;
        }
        while (i < 10) {
            buffer[i++] = (byte) (seqNumCopy & 0xFF);
            seqNumCopy >>= 8;
        }
        while (i < 138 && j < content.length)
            buffer[i++] = content[j++];

        return buffer;
    }

    int generateHash() {
        // Start with a non-zero constant (prime number).
        int result = 17;  
        // Incorporate srcId (short) into the hash
        result = 31 * result + Short.hashCode(srcId);
        // Incorporate seqNum (long) into the hash
        result = 31 * result + Long.hashCode(seqNum);
        // Incorporate content (byte[]) into the hash
        if (content != null)
            result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
