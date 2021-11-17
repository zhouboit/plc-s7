package simemens;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author bo.zhou
 * @date 2019/11/21
 */
public class S7Client {
    private static Logger logger = Logger.getLogger(S7Client.class);
    byte[] plcHead1 = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x11, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xC0, (byte) 0x01, (byte) 0x0A, (byte) 0xC1, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0xC2, (byte) 0x02, (byte) 0x01, (byte) 0x00};
    byte[] plcHead2 = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x19, (byte) 0x02, (byte) 0xF0, (byte) 0x80, (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0xE0};
    byte[] plcOrderNumber = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x21, (byte) 0x02, (byte) 0xF0, (byte) 0x80, (byte) 0x32, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x01, (byte) 0x12, (byte) 0x04, (byte) 0x11, (byte) 0x44, (byte) 0x01, (byte) 0x00, (byte) 0xFF, (byte) 0x09, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00};
    byte[] plchead1200smart = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x11, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xC1, (byte) 0x02, (byte) 0x10, (byte) 0x00, (byte) 0xC2, (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0xC0, (byte) 0x01, (byte) 0x0A};
    byte[] plchead2200smart = new byte[]{(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x19, (byte) 0x02, (byte) 0xF0, (byte) 0x80, (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0xCC, (byte) 0xC1, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x03, (byte) 0xC0};
    private String ipAddress = "";
    private int port = 102;
    private String CurrentPlc = "";
    private Socket socket = null;

    public S7Client(String simemens, String ipAddress, int port) {
        this.port = port;
        this.setUp(simemens, ipAddress);
    }


    public S7Client(String simemens, String ipAddress) {
        this.setUp(simemens, ipAddress);
    }

    private void setUp(String simemens, String ipAddress) {
        int wordLength = 2;
        this.ipAddress = ipAddress;
        this.CurrentPlc = simemens;
        switch (simemens) {
            case "SiemensPLCS.S1200":
                this.plcHead1[21] = 0;
                break;
            case "SiemensPLCS.S300":
                this.plcHead1[21] = 2;
                break;
            case "SiemensPLCS.S400":
                this.plcHead1[21] = 3;
                this.plcHead1[17] = 0x00;
                break;
            case "SiemensPLCS.S1500":
                this.plcHead1[21] = 0;
                break;
            case "SiemensPLCS.S200Smart":
                this.plcHead1 = this.plchead1200smart;
                this.plcHead2 = this.plchead2200smart;
                break;
            default:
                plcHead1[18] = 0;
        }
    }

    public synchronized void initConnection() {
        try {
            if (this.socket == null) {
                socket = new Socket();
                socket.connect(new InetSocketAddress(this.ipAddress, this.port));
                this.send(this.plcHead1);
                byte[] head1 = new byte[4];
                byte[] head2 = new byte[4];
                this.receive(head1, 0, 4);
                int content1 = head1[2] * 256 + head1[3] - 4;
                printView(head1);
                byte[] body1 = new byte[content1];
                this.receive(body1, 0, content1);
                printView(body1);
                this.send(this.plcHead2);
                receive(head2, 0, 4);
                printView(head2);
                int content2 = head2[2] * 256 + head2[3] - 4;
                byte[] body2 = new byte[content2];
                this.receive(body2, 0, content2);
                printView(body2);
            }
        } catch (Exception e) {
            logger.error("init connection error", e);
        }
    }

    private synchronized byte[] read(String address, int length) throws IOException {
        AnalysisData analysisData = Address.analysis(address);
        byte[] cmd = BuildCmd.buildReadCmd(analysisData.getType(), analysisData.offset, analysisData.getDbLabel(), length);
        printView(cmd);
        this.send(cmd);
        byte[] dataHead = new byte[4];
        this.receive(dataHead, 0, 4);
        int dataLength = dataHead[2] * 256 + dataHead[3] - 4;
        printView(dataHead);
        byte[] data = new byte[dataLength];
        this.receive(data, 0, dataLength);
        printView(data);
        byte[] result = new byte[4 + data.length];
        System.arraycopy(dataHead, 0, result, 0, 4);
        System.arraycopy(data, 0, result, 4, dataLength);
        byte[] content = new byte[length];
        System.arraycopy(result, 25, content, 0, length);
        return content;
    }

    private synchronized void write(String address, byte[] valueBytes) throws IOException {
        AnalysisData analysisData = Address.analysis(address);
        byte[] cmd = BuildCmd.buildWriteCmd(analysisData.getType(), analysisData.offset, analysisData.getDbLabel(), valueBytes);
        printView(cmd);
        this.send(cmd);
        byte[] dataHead = new byte[4];
        this.receive(dataHead, 0, 4);
        int dataLength = dataHead[2] * 256 + dataHead[3] - 4;
        printView(dataHead);
        byte[] data = new byte[dataLength];
        this.receive(data, 0, dataLength);
        printView(data);
    }

    public int readInt(String address) throws Exception {
        return Convert.FromByte.stream(read(address, 4)).intValue();
    }

    public void writeInt(String address, int value) throws Exception {
        write(address, Convert.ToByte.stream(value).intByteArray());
    }

    public void writeFloat(String address, float value) throws Exception {
        write(address, Convert.ToByte.stream(value).floatByteArray());
    }

    public float readFloat(String address) throws Exception {
        return Convert.FromByte.stream(read(address, 4)).floatValue();
    }

    public void writeBoolean(String address, boolean value) throws Exception {
        write(address, Convert.ToByte.stream(value).booleanByteArray());
    }

    public boolean readBoolean(String address) throws Exception {
        return Convert.FromByte.stream(read(address, 1)).booleanValue();
    }

    public void writeString(String address, String value) throws Exception {
        write(address, value.getBytes());
    }

    public String readString(String address, int length) throws Exception {
        return new String(read(address, length));
    }

    public void writeChar(String address, char value) throws Exception {
        writeByte(address, (byte) value);
    }

    public char readChar(String address) throws Exception {
        return (char) Convert.FromByte.stream(read(address, 1)).byteValue();
    }

    public void writeByte(String address, byte value) throws Exception {
        write(address, Convert.ToByte.stream(value).byteArray());
    }

    public byte readByte(String address) throws Exception {
        return Convert.FromByte.stream(read(address, 1)).byteValue();
    }

    public void writeBit(String address, boolean value) throws Exception {
        if (address.contains(".")) {
            int v = readByte(address.substring(0, address.indexOf(".")));
            int bit = Integer.parseInt(address.substring(address.indexOf(".") + 1));
            String binaryVal = "00000000".concat(Integer.toBinaryString(Math.abs(v)));
            List<String> bitList = Arrays.asList(binaryVal.split(""));
            Collections.reverse(bitList);
            bitList.set(bit, String.valueOf(value ? 1 : 0));
            Collections.reverse(bitList);
            int data = Integer.valueOf(String.join("", bitList), 2);
            writeByte(address.substring(0, address.indexOf(".")), (byte) data);
        } else {
            writeByte(address, (byte) 1);
        }
    }

    public boolean readBit(String address) throws Exception {
        if (address.contains(".")) {
            int v = readByte(address.substring(0, address.indexOf(".")));
            int bit = Integer.parseInt(address.substring(address.indexOf(".") + 1));
            String binaryVal = "00000000".concat(Integer.toBinaryString(Math.abs(v)));
            List<String> bitList = Arrays.asList(binaryVal.split(""));
            Collections.reverse(bitList);
            return Integer.parseInt(bitList.get(bit)) == 1;
        } else {
            return readByte(address) > 0;
        }
    }


    public static void printView(byte[] b) {
        StringBuilder hex = new StringBuilder();
        for (byte b1 : b) {
            hex.append(" 0x");
            hex.append(Integer.toHexString(b1 >> 4 & 0x0F));
            hex.append(Integer.toHexString(b1 & 0x0F));
        }
        logger.info(Arrays.toString(b));
        logger.info(hex.toString());
    }

    private void send(byte[] data) throws IOException {
        try {
            this.socket.getOutputStream().write(data);
        } catch (IOException e) {
            throw e;

        }
    }

    private int receive(byte[] b, int start, int len) {
        int res;
        try {
            InputStream in = this.socket.getInputStream();
            int retry = 0;
            while ((in.available() <= 0) && (retry < 500)) {
                try {
                    if (retry > 0) {
                        Thread.sleep(1);
                    }
                    retry++;
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            res = 0;
            while ((in.available() > 0) && (len > 0)) {
                res = in.read(b, start, len);
                start += res;
                len -= res;
            }
            return res;
        } catch (final IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            logger.error("close socket exception");
        }
    }
}
