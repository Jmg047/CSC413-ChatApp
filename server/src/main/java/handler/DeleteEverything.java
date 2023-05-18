package handler;

import dao.MessageDao;
import dto.MessageDto;
import handler.AuthFilter.AuthResult;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;
import java.util.Iterator;
import java.util.List;

public class DeleteEverything implements handler.BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        AuthResult authResult = handler.AuthFilter.doFilter(request);
        System.out.println(authResult.userName +"confirmed, target aquired." );
        var messageFilter = new Document("fromId", authResult.userName);
        List<MessageDto> messageDtoList = MessageDao.getInstance().query(messageFilter);
        Iterator<MessageDto> iterator = messageDtoList.iterator();
        while(iterator.hasNext()){
            MessageDto messageDto= iterator.next();
            if(messageDto.fromId.equals(authResult.userName)){
                iterator.remove();

            }
        }
        System.out.println(authResult.userName+", deleted.");
        var res = new RestApiAppResponse<>(true,messageDtoList , null);
        return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
    }

}