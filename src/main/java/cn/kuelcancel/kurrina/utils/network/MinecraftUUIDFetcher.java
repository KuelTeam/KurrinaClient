package cn.kuelcancel.kurrina.utils.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MinecraftUUIDFetcher {
    public static String getUUID(String id) {
        String currentID = Kurrina.getInstance().getAccountManager().getCurrentAccount().getName();
        String urlStr = "https://api.mojang.com/users/profiles/minecraft/" + id;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = bf.readLine()) != null) {
                    response.append(inputLine);
                }
                bf.close();
                System.out.println(response.toString());
                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
                return jsonObject.get("id").getAsString();
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                KurrinaLogger.error("Failed to fetch UUID for " + currentID + " because the player does not exist.");
            } else {
                KurrinaLogger.error("Failed to fetch UUID for " + currentID + " because of an unknown error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
