package handler;

import dao.FavDao;
import dao.UserDao;
import dto.BaseDto;
import dto.FavDto;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class CreateFavListHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        //convert the body to a dto of type FavDto
        FavDto favoriteDto = GsonTool.gson.fromJson(request.getBody(), dto.FavDto.class);
        FavDao favoriteDao = FavDao.getInstance();
        UserDao userDao = UserDao.getInstance();

        //check if the user is logged in
        AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);
        if(!authResult.isLoggedIn){
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        //check if the user we want to add to favorites is already in the database of users
        if (userDao.query(new Document("userName", favoriteDto.getFavId())).size() == 0) {
            var res = new RestApiAppResponse<>(false, null,
                    "Adding an unknown user");
            return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
        }

        //check if the user is already in the favorites list
        if(favoriteDao.queryByUserNameAndFavId(authResult.userName, favoriteDto.getFavId()).size() != 0){
            var res = new RestApiAppResponse<>(false, null,
                    "user was already added");
            return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
        }

        //add the user to the favorites list database
        favoriteDto.setUserName(authResult.userName);
        favoriteDao.put(favoriteDto);

        var res = new RestApiAppResponse<>(true, List.of(favoriteDto), null);
        return new HttpResponseBuilder().setStatus("200 OK").setBody(res);
    }
}
