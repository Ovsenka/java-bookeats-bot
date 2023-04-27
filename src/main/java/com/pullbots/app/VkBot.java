package com.pullbots.app;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.responses.GetLongPollServerResponse;
import com.vk.api.sdk.queries.groups.GroupsGetLongPollServerQuery;
import java.io.IOException;

public class VkBot {

    private static final String ACCESS_TOKEN = "";
    private static final int GROUP_ID = 0;
    private static String url;

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException, IOException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        GroupActor actor = new GroupActor(GROUP_ID, ACCESS_TOKEN);
        GroupCommandHandler handler = new GroupCommandHandler(vk, actor);

        GroupsGetLongPollServerQuery serverQuery = vk.groups().getLongPollServer(actor,GROUP_ID);
        GetLongPollServerResponse response = serverQuery.execute();
        String key = response.getKey();
        String serverUrl = response.getServer();
        Integer ts = Integer.valueOf(response.getTs());

        while (true) {
            url = serverUrl + "?act=a_check&key=" + key + "&ts=" + ts + "&wait=25";
            Thread.sleep(2500);
            String json = VkApiClientUtils.loadJson(url);
            System.out.println(json);
            Gson gs = new Gson();
            JsonObject messageObject = gs.fromJson(json, JsonObject.class);
            ts = messageObject.get("ts").getAsInt();
            JsonArray msg_array = messageObject.getAsJsonArray("updates");
            for (JsonElement ob: msg_array) handler.handle(ob.getAsJsonObject());
        }
    }
}
