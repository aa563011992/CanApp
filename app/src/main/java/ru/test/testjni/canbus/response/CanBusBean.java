package ru.test.testjni.canbus.response;

import java.io.Serializable;

/**
 * @author wyl
 * @description:
 * @date :2021/1/4 17:07
 */
public class CanBusBean implements Serializable {
    private byte[] canId;
    private byte[] data;

    public byte[] getCanId() {
        return canId;
    }

    public void setCanId(byte[] canId) {
        this.canId = canId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
