package handler;

import dao.ConversationDao;
import dao.MessageDao;
import dto.MessageDto;
import handler.AuthFilter.AuthResult;
import org.bson.Document;
import request.ParsedRequest;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class GetMostRecentConversationHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        //authorizing user
        AuthResult authResult = AuthFilter.doFilter(request);
        if(!authResult.isLoggedIn){
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }
        System.out.println(authResult.userName + " authorized.");

        //filtering for latest message to derive the latest conversation
        var messageFilter = new Document("fromId", authResult.userName);
        //grab messages from current user
        List<MessageDto> messageDtoList = MessageDao.getInstance().query(messageFilter);
        MessageDto latestMessage = messageDtoList.get(0);
        System.out.println("filtering for latest convo...");
        for(MessageDto message : messageDtoList){
            if (message.getTimestamp() >= latestMessage.getTimestamp()){
                latestMessage = message;
            }
        }
        System.out.println("...finished!");

        //grabbing messages from the latest conversation
        var latestMessageFilter = new Document("conversationId", latestMessage.getConversationId());
        //grab messages from convo based off latest message
        List<MessageDto> latestMessageDtoList = MessageDao.getInstance().query(latestMessageFilter);
        System.out.println("filtering for messages from latest convo...");
        List<MessageDto> latestConvoMessages = new ArrayList<>();
        for(MessageDto message : latestMessageDtoList) {
            if (message.getConversationId().equals(latestMessage.getConversationId())){
                System.out.println("found a message from the latest convo");
                latestConvoMessages.add(message);
            }
        }
        System.out.println("...finished!");

        var res = new RestApiAppResponse<>(true, latestConvoMessages, null);
        System.out.println("sending off the data");
        return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
    }

}