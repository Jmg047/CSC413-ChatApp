package handler;

import dao.MessageDao;
import dto.MessageDto;
import handler.AuthFilter.AuthResult;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;
import java.util.List;
import java.util.ArrayList;
public class SearchBarHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        AuthResult authResult = handler.AuthFilter.doFilter(request);
        var messageFilter = new Document("fromId", authResult.userName);
        // Retrieve all messages from the data source
        List<MessageDto> messageDtoList = MessageDao.getInstance().query(messageFilter);

        // Search for messages that match the input
        /*String searchInput = request.getQueryParam("searchTerm");
        List<MessageDto> searchResults = new ArrayList<>();
        for (MessageDto messageDto : messageDtoList) {
            if (messageDto.getMessage().contains(searchInput)) {
                searchResults.add(messageDto);
            }
        }*/

        var res = new RestApiAppResponse<>(true, searchResults, null);
        return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
    }
}
