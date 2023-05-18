package dto;

import org.bson.Document;

public class FavDto extends BaseDto {
    private String userName;
    private String favId;

    public String getFavId() {
        return favId;
    }

    public FavDto setFavId(String favId) {
        this.favId = favId;
        return this;
    }

    public FavDto() {
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Document toDocument(){
        return new Document()
                .append("userName", userName)
                .append("favId", favId);
    }

    public static FavDto fromDocument(Document match) {
        var favDto = new FavDto();
        favDto.setUserName(match.getString("userName"));
        favDto.setFavId(match.getString("favId"));
        return favDto;
    }
}
