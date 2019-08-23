package com.yufan.utils;

import java.io.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/1 16:36
 * 功能介绍:  对象序列化与反序列化（对象必须实现Serializable）
 *      注意：对象序列化与反序列化对象所在的包名必须相同
 */
public class SerializeUtil {

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            return object;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
        }
    }

}
