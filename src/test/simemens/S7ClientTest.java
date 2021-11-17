package simemens;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author bo.zhou
 * @date 2019/11/22
 */
public class S7ClientTest {
    private S7Client s7Client = null;

    @Before
    public void setUp() throws Exception {
        this.s7Client = new S7Client("SiemensPLCS.S1200", "10.10.197.20");
        this.s7Client.initConnection();
    }

    @After
    public void tearDown() throws Exception {
        this.s7Client.close();
    }

    @Test
    public void buildCmd() throws IOException {
        AnalysisData analysisData = Address.analysis("10.10.197.20");
        byte[] cmd = BuildCmd.buildWriteCmd(analysisData.getType(), analysisData.getOffset(), analysisData.getDbLabel(), Convert.ToByte.stream(12345678).intByteArray());
        S7Client.printView(cmd);
    }

    @Test
    public void writeInt() throws Exception {
        this.s7Client.writeInt("M101", 123456789);
    }

    @Test
    public void readInt() throws Exception {
        int M101 = this.s7Client.readInt("M101");
        System.out.println(M101);
    }

    @Test
    public void writeFloat() throws Exception {
        this.s7Client.writeFloat("M106", 7.8f);
    }

    @Test
    public void readFloat() throws Exception {
        float M106 = this.s7Client.readFloat("M106");
        System.out.println(M106);
    }

    @Test
    public void writeBoolean() throws Exception {
        this.s7Client.writeBoolean("M105", true);
    }

    @Test
    public void readBoolean() throws Exception {
        boolean M105 = this.s7Client.readBoolean("M105");
        System.out.println(M105);
    }

    @Test
    public void writeString() throws Exception {
        //英文一个字符占一个字节，中文一个字符三个字节
        this.s7Client.writeString("M110", "1234567啊");
    }

    @Test
    public void readString() throws Exception {
        String M110 = this.s7Client.readString("M110", 10);
        System.out.println(M110);

    }

    @Test
    public void writeChar() throws Exception {
        this.s7Client.writeChar("M211", 'A' );
    }

    @Test
    public void readChar() throws Exception {
        char c = this.s7Client.readChar("M211");
        System.out.println(c);
    }

    @Test
    public void writeByte() throws Exception {
        this.s7Client.writeByte("Q0", (byte) 3);
    }

    @Test
    public void readByte() throws Exception {
        String desc = "00 报警复位 01 蜂鸣器运行 10 报警红色灯 11 报警红色灯并且蜂鸣器运行";
        byte b = this.s7Client.readByte("Q0");
        System.out.println(b);
    }

    public static void main(String[] args) {
        byte[] arrays = new byte[1];
        arrays[0]=(byte) (0xFA & 0xFFFF);
        S7Client.printView(arrays);
    }
}