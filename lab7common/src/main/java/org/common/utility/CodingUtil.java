package org.common.utility;

import java.nio.ByteBuffer;

public class CodingUtil {
    public static byte[] encodingInt(int number){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        // Записываем int в ByteBuffer
        buffer.putInt(number);
        // Получаем массив байтов
        return buffer.array();
    }
    public static int decodingInt(byte[] bytes){
        // Создаем ByteBuffer из байтового массива
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        // Читаем int из ByteBuffer
        int number = buffer.getInt();

        return number;
    }
}
