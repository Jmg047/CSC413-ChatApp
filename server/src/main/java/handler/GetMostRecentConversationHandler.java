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

import java.util.ArrayList;
import java.util.List;

public class GetMostRecentConversationHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        AuthResult authResult = AuthFilter.doFilter(request);
        if(!authResult.isLoggedIn){
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        var messageFilter = new Document("fromId", authResult.userName);
        List<MessageDto> messageDtoList = MessageDao.getInstance().query(messageFilter);
        MessageDto latestMessage = messageDtoList.get(0);
        for(MessageDto message : messageDtoList){
            if (message.getTimestamp() >= latestMessage.getTimestamp()){
                latestMessage = message;
            }
        }

        List<MessageDto> latestConvoMessages = new ArrayList<>();
        for(MessageDto message : messageDtoList) {
            if (message.getToId().equals(latestMessage.getToId())){
                latestConvoMessages.add(message);
            }
        }
        var res = new RestApiAppResponse<>(true, latestConvoMessages, null);
        return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
    }

}