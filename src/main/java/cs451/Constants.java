package cs451;

public class Constants {
    public static final int ARG_LIMIT_CONFIG = 7;

    // indexes for id
    public static final int ID_KEY = 0;
    public static final int ID_VALUE = 1;

    // indexes for hosts
    public static final int HOSTS_KEY = 2;
    public static final int HOSTS_VALUE = 3;

    // indexes for output
    public static final int OUTPUT_KEY = 4;
    public static final int OUTPUT_VALUE = 5;

    // indexes for config
    public static final int CONFIG_VALUE = 6;

    // code for message
    public static final char MES = 'M';
    public static final char ACK = 'A';

    // size of various things
    public static final int BYTE_OF_CONTENT = 22;
    public static final int BYTE_PER_MES = 32;
    public static final int MES_PER_PAC = 8;
    public static final int BYTE_PER_PAC = 32*8;
}
