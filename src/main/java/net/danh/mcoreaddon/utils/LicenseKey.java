package net.danh.mcoreaddon.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.danh.mcoreaddon.MCoreAddon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LicenseKey {

    public static boolean checkKey() {
        String key = Files.getConfig().contains("license-key") ? Files.getConfig().getString("license-key") : null;
        if (key == null) {
            File configFile = new File(MCoreAddon.getMCore().getDataFolder(), "config.yml");
            FileConfiguration fileConfiguration = Files.getConfig();
            if (!fileConfiguration.contains("license-key"))
                fileConfiguration.set("license-key", "YOUR_LICENSE_KEY");
            try {
                fileConfiguration.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Files.reloadFile();
            YamlConfiguration.loadConfiguration(configFile);
            Files.reloadFile();
        }
        String urlString = "https://api.licensegate.io/license/a1d04/" + key + "/verify";
        try {
            // Create a URL object
            URL url = new URL(urlString);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // 200 OK
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // Close the connections
                in.close();
                connection.disconnect();
                // Parse the JSON response using Gson
                JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();

                // Check if "valid" is true
                boolean isValid = jsonResponse.get("valid").getAsBoolean();
                if (isValid) {
                    MCoreAddon.getMCore().getLogger().info("Valid license key - Verification successful");
                    return true;
                } else {
                    MCoreAddon.getMCore().getLogger().warning("Invalid license key - Verification failed");
                    MCoreAddon.getMCore().getLogger().warning("Join Discord to get License Key: https://discord.gg/2G7TarwWAV");
                    return false;
                }
            } else {
                MCoreAddon.getMCore().getLogger().warning("Failed to connect. Response code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
