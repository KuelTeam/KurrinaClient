package cn.kuelcancel.kurrina.irc;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.impl.IRCMod;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class ChatClient extends CSCommunicator {

    public static boolean isServerAvailable = false;
    public static int retryCount = 0;
    public static Minecraft mc = Minecraft.getMinecraft();
    IRCMod ircMod = (IRCMod) Kurrina.getInstance().getModManager().getModByTranslateKey(TranslateText.IRC.getKey());


    public static int doWhileToken = 0;
    public int currentToken = 0;

    @Override
    public void run() {
        try {
            long time = System.currentTimeMillis();
            doWhileToken = currentToken = new Random().nextInt();
            KurrinaLogger.info("IRC client starting with token " + currentToken);
            KurrinaLogger.info("Connecting to IRC server...");
            connect();
            listener = new ChatClientListener();
            sender = new ChatClientSender();
            listener.setName("IRC Listener");
            sender.setName("IRC Sender");
            listener.start();
            sender.start();

            KurrinaLogger.info("Initialized IRC client in " + (System.currentTimeMillis() - time) / 1000d + "s");
            ChatPacket packet = new ChatPacket();
            packet.sender = Kurrina.getInstance().getAccountManager().getCurrentAccount().getName();
            packet.senderUUID = Kurrina.getInstance().getAccountManager().getCurrentAccount().getUuid();
            packet.packetType = "join";
            sender.send(packet);

            isServerAvailable = true;
            flagHeartbeat();
            while (doWhileToken == currentToken && ircMod.isToggled()) {
                if ((!listener.isAlive()) || (!sender.isAlive())) {
                    throw new Exception("Sender or listener isn't alive, stopping...");
                }
            }
        } catch (Exception e) {
            KurrinaLogger.error("An error occurred in the online chat thread and it is stopped", e);
            isServerAvailable = false;
        } finally {
            listener.interrupt();
            sender.interrupt();
            try { socket.close(); } catch (IOException e) { }
            try { sleep(30000); } catch (InterruptedException e) { }
            ChatClientManager.refreshChatClient();
        }
    }

    public void reconnect() throws Exception{
        int retry = 3;
        while (retry > 0 && socket == null) {
            try {
                sleep(60000);
                KurrinaLogger.info("Connecting to IRC server...");
                try {
                    socket = new Socket("149.88.86.137", 831);
                } catch (IOException e) {
                    KurrinaLogger.error("Failed to connect to IRC server, please retry later", e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry --;
        }

        if (retry == 0) {
            throw new IOException("Failed to reconnect to the IRC server, refreshing the client");
        }
    }

    public void connect() throws Exception{
        if (socket == null) {
            KurrinaLogger.info("Connecting to IRC server...");
            try {
                socket = new Socket("149.88.86.137", 831);
            } catch (IOException e) {
                KurrinaLogger.error("Failed to connect to IRC server, please retry later", e);
                reconnect();
            }
        }
    }

    public void stopIRC() {
        try {
            socket = null;
            stopThreads();
        } catch (Exception e) {
            KurrinaLogger.error("Failed to stop IRC bot", e);
        }
    }

    private void stopThreads() {
        try {
            if (listener != null) listener.interrupt();
            if (sender != null) sender.interrupt();
            listener.stop();
            sender.stop();
            socket.close();
            doWhileToken = 0;
        } catch (Exception e) {
            KurrinaLogger.error("Error occurred while stopping listener or sender thread", e);
        }
    }

    public ChatClientSender getSender() {
        return sender;
    }
}
