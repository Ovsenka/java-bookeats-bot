package com.pullbots.app;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.ArrayList;
import java.util.Random;

public class GroupCommandHandler {
    private ArrayList<EQueue> qList = new ArrayList<>();
    private VkApiClient vkClient;
    private VkApiClientUtils utils;
    private GroupActor actor;
    private Random gen;
    private Integer actual_ts;
    {
        gen = new Random();
        actual_ts = 0;
    }
    public GroupCommandHandler(VkApiClient vkClient, GroupActor actor) {
        this.vkClient = vkClient;
        this.actor = actor;
        utils = new VkApiClientUtils(vkClient, actor);
    }

    void handle(JsonObject messageObject) throws ClientException, ApiException {
        System.out.println(messageObject.toString());
        String type = messageObject.get("type").getAsString();
        if (type.equals("message_new"))
        {
            String text = messageObject.getAsJsonObject("object").getAsJsonObject("message").get("text").getAsString();
            Integer groupChatId = messageObject.getAsJsonObject("object").getAsJsonObject("message").get("peer_id").getAsInt();
            Integer msgId = messageObject.getAsJsonObject("object").getAsJsonObject("message").get("id").getAsInt();
            System.out.println("NEW MSG: "+text+" - chat "+groupChatId+" - msgId "+msgId);
            if (text.equals("/ping")){
                vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("pong").execute();
            } if (text.equals("/hi")){
                vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("Привет-привет!").execute();
            } if (text.equals("/q")){
                if (qList.isEmpty())
                    vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("Очередей нет.").execute();
                else {
                    vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("Очереди:").execute();
                    for (EQueue q : qList){
                        vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message(q.discipline).execute();
                        vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message(q.getStrParticipants()).execute();
                    }
                }
            } if (text.split(" ")[0].equals("/new")){
                String discipline = text.split(" ")[1];
                vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("Создаю очередь: "+discipline).execute();
                qList.add(new EQueue(discipline));
            } if (text.split(" ")[0].equals("/+")){
                String discipline = text.split(" ")[1];
                for (EQueue eQ: qList){
                    if (eQ.discipline.equals(discipline)){
                        Integer userId = messageObject.getAsJsonObject("object").getAsJsonObject("message").get("from_id").getAsInt();
                        String name = utils.getUserFirstnameById(userId);
                        String lastname = utils.getUserLastnameById(userId);
                        if (!eQ.userIsInQueue(userId)) {
                            vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("Добавляю в очередь: "+discipline).execute();
                            eQ.addUser(new VkUser(userId, name, lastname));
                            break;
                        }
                        else
                            vkClient.messages().send(actor).replyTo(msgId).peerId(groupChatId).randomId(gen.nextInt()).message("Вы уже встали в очередь "+discipline+"!").execute();
                    }
                }
            }


        }
    }
}
