package org.springbus.test.tio;

import org.tio.core.intf.EncodedPacket;

public class StringPacket extends EncodedPacket {
    /**
     * @param bytes
     * @author tanyaowu
     */
    public StringPacket(byte[] bytes) {
        super(bytes);
    }

    @Override
    public String toString() {
        return new String(getBytes());
    }
}
