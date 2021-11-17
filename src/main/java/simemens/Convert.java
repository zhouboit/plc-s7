package simemens;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * base data type convert with byte array
 *
 * @author bo.zhou
 * @date 2019/11/22
 */
public class Convert {

    public static class ToByte {

        private static Logger logger = Logger.getLogger(ToByte.class);

        private DataOutputStream dataOutputStream;
        private ByteArrayOutputStream byteArrayOutputStream;
        private Object object;

        private ToByte(DataOutputStream dataOutputStream, ByteArrayOutputStream byteArrayOutputStream, Object object) {
            this.dataOutputStream = dataOutputStream;
            this.byteArrayOutputStream = byteArrayOutputStream;
            this.object = object;
        }

        public static ToByte stream(Object object) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            return new ToByte(dataOutputStream, byteArrayOutputStream, object);
        }

        public byte[] intByteArray() {
            try {
                if (this.object == null) {
                    return new byte[0];
                }
                this.dataOutputStream.writeInt((Integer) this.object);
                return this.byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                logger.error("get byte array from int exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return new byte[0];
        }

        public byte[] booleanByteArray() {
            try {
                if (this.object == null) {
                    return new byte[0];
                }
                this.dataOutputStream.writeBoolean((Boolean) this.object);
                return this.byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                logger.error("get byte array from boolean exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return new byte[0];
        }

        public byte[] floatByteArray() {
            try {
                if (this.object == null) {
                    return new byte[0];
                }
                this.dataOutputStream.writeFloat((Float) this.object);
                return this.byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                logger.error("get byte array from float exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return new byte[0];
        }

        public byte[] byteArray() {
            try {
                if (this.object == null) {
                    return new byte[0];
                }
                this.dataOutputStream.writeByte((byte) this.object);
                return this.byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                logger.error("get byte array from byte exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return new byte[0];
        }

        public byte[] charByteArray() {
            try {
                if (this.object == null) {
                    return new byte[0];
                }
                this.dataOutputStream.writeChar((char) this.object);
                return this.byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                logger.error("get byte array from char exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return new byte[0];
        }

        private void close() throws IOException {
            this.dataOutputStream.close();
            this.byteArrayOutputStream.close();
        }


    }

    public static class FromByte {

        private static Logger logger = Logger.getLogger(FromByte.class);
        private DataInputStream dataInputStream;
        private ByteArrayInputStream byteArrayInputStream;

        private FromByte(DataInputStream dataInputStream, ByteArrayInputStream byteArrayInputStream) {
            this.dataInputStream = dataInputStream;
            this.byteArrayInputStream = byteArrayInputStream;
        }

        public static FromByte stream(byte[] bytes) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            return new FromByte(dataInputStream, byteArrayInputStream);
        }

        public int intValue() {
            try {
                return this.dataInputStream.readInt();
            } catch (IOException e) {
                logger.error("get int value exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return 0;
        }

        public boolean booleanValue() {
            try {
                return this.dataInputStream.readBoolean();
            } catch (IOException e) {
                logger.error("get boolean value exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return false;
        }

        public float floatValue() {
            try {
                return this.dataInputStream.readFloat();
            } catch (IOException e) {
                logger.error("get float value exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return 0f;
        }

        public char charValue() {
            try {
                return this.dataInputStream.readChar();
            } catch (IOException e) {
                logger.error("get char value exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return 0;
        }

        public byte byteValue() {
            try {
                return this.dataInputStream.readByte();
            } catch (IOException e) {
                logger.error("get byte value exception ", e);
            } finally {
                try {
                    close();
                } catch (IOException e) {
                    logger.error("close stream exception ", e);
                }
            }
            return 0;
        }

        private void close() throws IOException {
            dataInputStream.close();
            byteArrayInputStream.close();
        }
    }


}
