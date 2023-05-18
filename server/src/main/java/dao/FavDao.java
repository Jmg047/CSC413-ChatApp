package dao;

import com.mongodb.client.MongoCollection;
import dto.BaseDto;
import dto.FavDto;
import dto.UserDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavDao extends BaseDao<FavDto>{
    private static FavDao instance;

    protected FavDao(MongoCollection collection) {
        super(collection);
    }

    public static FavDao getInstance(){
        if(instance != null){
            return instance;
        }
        instance = new FavDao(MongoConnection.getCollection("FavDao"));
        return instance;
    }

    public static FavDao getInstance(MongoCollection collection){
        instance = new FavDao(collection);
        return instance;
    }


    @Override
    public void put(FavDto favDto) {
        collection.insertOne(favDto.toDocument());
    }


    @Override
    public List<FavDto> query(Document filter) {
        return collection.find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(FavDto::fromDocument)
                .collect(Collectors.toList());
    }


    public List<FavDto> queryByUserNameAndFavId(String userName, String favId){
        return query(new Document("userName", userName).append("favId", favId));
    }

}
