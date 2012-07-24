package org.spout.vanilla.protocol.handler;

import org.spout.api.Spout;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.protocol.msg.ClientStatusMessage;

public class ClientStatusHandler extends MessageHandler<ClientStatusMessage> {
    @Override
    public void handleServer(Session session, Player player, ClientStatusMessage message) {
        //TODO Do we handle anything? Should we send chunks when the client says "I am a'okay to log in?", what does "I am a'okay to login even mean?"
        //For now lets print out when the client sends it
        Spout.log("Client sent: " + message.toString());
    }
}
