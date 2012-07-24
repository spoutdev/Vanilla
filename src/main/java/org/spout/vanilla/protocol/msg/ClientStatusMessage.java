package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public class ClientStatusMessage extends Message {
    public static final byte INITIAL_SPAWN = 0;
    public static final byte RESPAWN = 1;
    private final byte status;

    public ClientStatusMessage(byte status) {
        this.status = status;
    }

    public byte getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
                .append("status", status)
                .toString();
    }
}
