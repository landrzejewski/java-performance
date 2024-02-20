package pl.training.performance.flatfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRow implements Row<Long> {

    private static final int FIRST_NAME_SIZE = 50;
    private static final int LAST_NAME_SIZE = 70;
    private static final int RECORD_SIZE = LONG_SIZE + FIRST_NAME_SIZE + LAST_NAME_SIZE + INT_SIZE + BOOL_SIZE;

    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private boolean isActive;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public byte[] toBytes() {
        var buffer = ByteBuffer.allocate(RECORD_SIZE);
        buffer.putLong(id);
        buffer.put(toField(firstName.getBytes(CHARSET), FIRST_NAME_SIZE));
        buffer.put(toField(lastName.getBytes(CHARSET), LAST_NAME_SIZE));
        buffer.putInt(age);
        buffer.put(toField(isActive));
        return buffer.array();
    }

    @Override
    public Row fromBytes(byte[] bytes) {
        var buffer = ByteBuffer.wrap(bytes);
        return PersonRow.builder()
                .id(buffer.getLong())
                .firstName(getString(readNextBytes(buffer, FIRST_NAME_SIZE)))
                .lastName(getString(readNextBytes(buffer, LAST_NAME_SIZE)))
                .age(buffer.getInt())
                .isActive(getBoolean(readNextBytes(buffer, BOOL_SIZE)))
                .build();
    }

    private byte[] readNextBytes(ByteBuffer buffer, int size) {
        var bytes = new byte[size];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    public int getSize() {
        return RECORD_SIZE;
    }

}
