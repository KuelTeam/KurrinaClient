package cn.kuelcancel.kurrina.irc;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.impl.IRCMod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatClientSender extends CSCommunicator {
    IRCMod ircMod = (IRCMod) Kurrina.getInstance().getModManager().getModByTranslateKey(TranslateText.IRC.getKey());
    private static final Queue<ChatPacket> packetQueue = new ConcurrentLinkedDeque<>();
    @Override
    public void run() {
        if (ircMod.isToggled()) {
            try {
                while(true) {
                    try {
                        Thread.sleep(350);
                    } catch (InterruptedException e) {
                    }
                    while(!packetQueue.isEmpty()) {
                        ChatPacket packet = packetQueue.poll();
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        buffer.write(HEADER);
                        buffer.write(packet.getBuffer());
                        buffer.write(END);

                        socket.getOutputStream().write(buffer.toByteArray());

                        if(!packet.packetType.equals("heartbeat")) {
                            KurrinaLogger.info("Send packet: " + packet.getJson());
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(ChatPacket packet) {
        if (ircMod.isToggled()) {
            packetQueue.add(packet);
        }
    }

    public void sendMessage(String msg) {
        if (ircMod.isToggled()) {
            ChatPacket packet = new ChatPacket();
            packet.message = msg;
            packet.packetType = "chat";
            packet.senderUUID = Kurrina.getInstance().getAccountManager().getCurrentAccount().getUuid();
            packet.sender = Kurrina.getInstance().getAccountManager().getCurrentAccount().getName();
            send(packet);
        }
    }

    public void sendHeartbeat() {
        if (ircMod.isToggled()) {
            ChatPacket packet = new ChatPacket();
            packet.packetType = "heartbeat";
            send(packet);
        }
    }

}
