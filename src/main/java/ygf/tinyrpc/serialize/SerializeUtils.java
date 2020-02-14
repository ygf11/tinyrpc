package ygf.tinyrpc.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化和反序列化的工具类
 *
 * @author theo
 * @date 20181128
 */
public class SerializeUtils {

    private static Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    /**
     * 对象转成byte数组(字节流)
     *
     * @param object
     * @return
     */
    public static byte[] objectToByteArray(Object object) throws Exception {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        bytes = bos.toByteArray();
        oos.close();
        bos.close();

        return bytes;
    }

    /**
     * byte数组(字节流)转换成对象
     *
     * @param bytes
     * @return
     */
    public static Object byteArrayToObject(byte[] bytes) throws Exception {
        Object object;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        object = ois.readObject();
        ois.close();
        bis.close();
        return object;
    }
}
