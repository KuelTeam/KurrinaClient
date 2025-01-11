package cn.kuelcancel.kurrina.discord;

import java.time.OffsetDateTime;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.discord.ipc.IPCClient;
import cn.kuelcancel.kurrina.discord.ipc.IPCListener;
import cn.kuelcancel.kurrina.discord.ipc.entities.RichPresence;
import cn.kuelcancel.kurrina.discord.ipc.exceptions.NoDiscordClientException;

public class DiscordRPC {

	private IPCClient client;
	
	public void start() {
		
		client = new IPCClient(1059341815205068901L);
		client.setListener(new IPCListener() {
			@Override
			public void onReady(IPCClient client) {
				
				RichPresence.Builder builder = new RichPresence.Builder();
				
				builder.setState("Playing Kurrina Client v" + Kurrina.getInstance().getVersion())
						.setStartTimestamp(OffsetDateTime.now())
						.setLargeImage("icon");
				
				client.sendRichPresence(builder.build());
			}
		});
		
		try {
			client.connect();
		} catch (NoDiscordClientException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		client.close();
	}

	public IPCClient getClient() {
		return client;
	}
	
	public boolean isStarted() {
		return client != null;
	}
}
