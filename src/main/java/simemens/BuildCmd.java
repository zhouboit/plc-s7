package simemens;

/**
 * @author bo.zhou
 * @date 2019/11/21
 */
public class BuildCmd {

    public static byte[] buildReadCmd(int type, int offset, int dbLabel, int length) {
        //适配批量读
        int[] types = {type};
        int[] offsets = {offset};
        int[] dbLabels = {dbLabel};
        int[] lengths = {length};
        byte readCount = (byte) lengths.length;
        byte[] commandByteArray = new byte[19 + readCount * 12];
        // 报文头
        commandByteArray[0] = (byte) 0x03;
        commandByteArray[1] = (byte) 0x00;
        // 长度
        commandByteArray[2] = (byte) (commandByteArray.length / 256);
        commandByteArray[3] = (byte) (commandByteArray.length % 256);
        // 固定
        commandByteArray[4] = (byte) 0x02;
        commandByteArray[5] = (byte) 0xF0;
        commandByteArray[6] = (byte) 0x80;
        // 协议标识
        commandByteArray[7] = (byte) 0x32;
        // 命令：发
        commandByteArray[8] = (byte) 0x01;
        // redundancy identification (reserved): 0x0000;
        commandByteArray[9] = (byte) 0x00;
        // protocol data unit reference; it’s increased by request event;
        commandByteArray[10] = (byte) 0x00;
        commandByteArray[11] = (byte) 0x00;
        // 参数命令数据总长度
        commandByteArray[12] = (byte) 0x01;
        commandByteArray[13] = (byte) ((commandByteArray.length - 17) / 256);
        commandByteArray[14] = (byte) ((commandByteArray.length - 17) % 256);
        // 读取内部数据时为00，读取CPU型号为Data数据长度
        commandByteArray[15] = (byte) 0x00;
        commandByteArray[16] = (byte) 0x00;
        // 读写指令，04读，05写
        commandByteArray[17] = (byte) 0x04;
        // 读取数据块个数
        commandByteArray[18] = readCount;
        for (byte i = 0; i < readCount; i++) {
            commandByteArray[19 + i * 12] = (byte) 0x12;
            // 接下来本次地址访问长度
            commandByteArray[20 + i * 12] = (byte) 0x0A;
            // 语法标记，ANY
            commandByteArray[21 + i * 12] = (byte) 0x10;
            // 按字为单位
            commandByteArray[22 + i * 12] = (byte) 0x02;
            // 访问数据的个数
            commandByteArray[23 + i * 12] = (byte) (lengths[i] / 256);
            commandByteArray[24 + i * 12] = (byte) (lengths[i] % 256);
            // DB块编号，如果访问的是DB块的话
            commandByteArray[25 + i * 12] = (byte) (dbLabels[i] / 256);
            commandByteArray[26 + i * 12] = (byte) (dbLabels[i] % 256);
            // 访问数据类型
            commandByteArray[27 + i * 12] = (byte) (types[i]);
            // 偏移位置
            commandByteArray[28 + i * 12] = (byte) (offsets[i] / 256 / 256 % 256);
            commandByteArray[29 + i * 12] = (byte) (offsets[i] / 256 % 256);
            commandByteArray[30 + i * 12] = (byte) (offsets[i] % 256);
        }
        return commandByteArray;
    }

    public static byte[] buildWriteCmd(int type, int offset, int dbLabel, byte[] data) {
        //适配批量读
        byte[] commandByteArray = new byte[35 + data.length];
        // 报文头
        commandByteArray[0] = (byte) 0x03;
        commandByteArray[1] = (byte) 0x00;
        // 长度
        commandByteArray[2] = (byte) ((35 + data.length) / 256);
        commandByteArray[3] = (byte) ((35 + data.length) % 256);
        // 固定
        commandByteArray[4] = (byte) 0x02;
        commandByteArray[5] = (byte) 0xF0;
        commandByteArray[6] = (byte) 0x80;
        // 协议标识
        commandByteArray[7] = (byte) 0x32;

        // 命令：发
        commandByteArray[8] = (byte) 0x01;

        // redundancy identification (reserved): 0x0000;
        commandByteArray[9] = (byte) 0x00;
        // protocol data unit reference; it’s increased by request event;
        commandByteArray[10] = (byte) 0x00;
        commandByteArray[11] = (byte) 0x00;
        // 参数命令数据总长度
        commandByteArray[12] = (byte) 0x01;


        commandByteArray[13] = (byte) 0x00;
        commandByteArray[14] = (byte) 0x0E;

        // 读取内部数据时为00，读取CPU型号为Data数据长度
        commandByteArray[15] = (byte) ((4 + data.length) / 256);
        commandByteArray[16] = (byte) ((4 + data.length) % 256);

        // 读写指令，04读，05写
        commandByteArray[17] = (byte) 0x05;

        // 读取数据块个数
        commandByteArray[18] = 0x01;

        // 固定，返回数据长度
        commandByteArray[19] = 0x12;
        commandByteArray[20] = 0x0A;
        commandByteArray[21] = 0x10;

        // 写入方式，1是按位，2是按字节
        commandByteArray[22] = 0x02;
        // 写入数据的个数
        commandByteArray[23] = (byte) (data.length / 256);
        commandByteArray[24] = (byte) (data.length % 256);

        // DB块编号，如果访问的是DB块的话
        commandByteArray[25] = (byte) (dbLabel / 256);
        commandByteArray[26] = (byte) (dbLabel % 256);

        // 写入数据的类型
        commandByteArray[27] = (byte) type;

        // 偏移位置
        commandByteArray[28] = (byte) (offset / 256 / 256 % 256);
        commandByteArray[29] = (byte) (offset / 256 % 256);
        commandByteArray[30] = (byte) (offset % 256);

        // 按字写入
        commandByteArray[31] = 0x00;
        commandByteArray[32] = 0x04;

        // 按位计算的长度
        commandByteArray[33] = (byte) (data.length * 8 / 256);
        commandByteArray[34] = (byte) (data.length * 8 % 256);
        System.arraycopy(data, 0, commandByteArray, 35, data.length);
        return commandByteArray;
    }
}
