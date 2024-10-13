package cs451.dataType;

import java.util.ArrayList;
import java.util.List;

import cs451.Constants;

public class Packet {
    boolean isAck;
    short srcId;
    long pacNum;
    byte[] data;

    Packet(short srcId, long pacNum, List<Message> messages) {
        this.isAck = false;
        this.srcId = srcId;
        this.pacNum = pacNum;
        this.data = new byte[Constants.BYTE_PER_PAC];
        for (int i = 0; i < messages.size(); i++) {
            byte[] mesBytes = messages.get(i).getBytes();
            for (int j = 0; j < Constants.BYTE_PER_MES; j++)
                data[Constants.BYTE_PER_MES*i+j] = mesBytes[j];
        }
    }

    Packet(short srcId, long pacNum) {
        this.isAck = true;
        this.srcId = srcId;
        this.pacNum = pacNum;
    }

    List<Message> decode() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < Constants.MES_PER_PAC; i++) {
            int start = Constants.BYTE_PER_MES*i;

            short srcId = (short)((data[start+1] << 8) | data[start]);
            if (srcId == 0)
                break;

            int seqNum = 0;
            for (int j = start+9; j >= start+2; j--)
                seqNum = (seqNum << 8) | data[i];

            byte[] content = new byte[Constants.BYTE_OF_CONTENT];
            int m = 0;
            int n = start+10;
            while (m < Constants.BYTE_OF_CONTENT)
                content[m++] = data[n++];
 
            messages.add(new Message(srcId, seqNum, content));
        }
        return messages;
    }

    @Override
    public String toString() {
        return srcId + ":" + pacNum; 
    }


}
