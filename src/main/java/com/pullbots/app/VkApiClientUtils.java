package com.pullbots.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;


public class VkApiClientUtils {
    private VkApiClient vkClient;
    private GroupActor actor;
    public VkApiClientUtils(VkApiClient vkClient, GroupActor actor) {
        this.vkClient = vkClient;
        this.actor = actor;
    }

    public static String loadJson(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    public String getUserFirstnameById(Integer userId) throws ClientException, ApiException {
        return vkClient.users().get(actor).userIds(String.valueOf(userId)).execute().get(0).getFirstName();
    }
    public String getUserLastnameById(Integer userId) throws ClientException, ApiException {
        return vkClient.users().get(actor).userIds(String.valueOf(userId)).execute().get(0).getLastName();
    }

}
