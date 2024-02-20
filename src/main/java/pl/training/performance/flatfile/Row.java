package pl.training.performance.flatfile;

import java.nio.charset.Charset;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface Row<I> {

    Charset CHARSET = UTF_8;
    int START_POSITION = 0;
    byte EMPTY_VALUE = 0x0;
    byte TRUE_VALUE = 0x1;
    byte FALSE_VALUE = 0x2;
    int LONG_SIZE = 8;
    int INT_SIZE = 4;
    int BOOL_SIZE = 1;

    I getId();

    byte[] toBytes();

    Row fromBytes(byte[] bytes);

    default byte[] toField(byte[] data, int size) {
        var bytes = new byte[size];
        System.arraycopy(data, START_POSITION, bytes, START_POSITION, data.length);
        Arrays.fill(bytes, data.length, size, EMPTY_VALUE);
        return bytes;
    }

    default byte[] toField(boolean data) {
        return new byte[]{data ? TRUE_VALUE : FALSE_VALUE};
    }

    default String getString(byte[] bytes) {
        var endIndex = START_POSITION;
        for (var index = bytes.length - 1; index >= START_POSITION; index--) {
            if (bytes[index] != EMPTY_VALUE) {
                endIndex = index + 1;
                break;
            }
        }
        return new String(Arrays.copyOfRange(bytes, 0, endIndex), CHARSET);
    }

    default boolean getBoolean(byte[] bytes) {
        return bytes[0] == TRUE_VALUE;
    }

    int getSize();

}
