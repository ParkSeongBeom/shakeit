package kr.co.mythings.shackit.core.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

public enum DeviceType {
    Web(1),
    iOS(2),
    Android(3);

    @Getter
    private final int value;
    DeviceType(int i) {
        this.value = i;
    }

    public static DeviceType fromInteger(int deviceTypeIdx) {
        return Arrays.stream(DeviceType.values()).filter(o -> o.getValue()==deviceTypeIdx).findFirst().orElse(null);
    }
}
