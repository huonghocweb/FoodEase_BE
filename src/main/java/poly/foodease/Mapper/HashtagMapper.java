package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.Hashtag;
import poly.foodease.Model.Request.HashtagRequest;
import poly.foodease.Model.Response.HashtagResponse;

@Mapper(componentModel = "spring")
public abstract class HashtagMapper {

    public HashtagResponse convertEnToRes(Hashtag hashtag) {
        return HashtagResponse.builder()
                .hashtagId(hashtag.getHashtagId())
                .hashtagName(hashtag.getHashtagName())
                .build();
    }

    public Hashtag convertReqToEn(HashtagRequest hashtagRequest) {
        return Hashtag.builder()
                .hashtagName(hashtagRequest.getHashtagName())
                .build();
    }
}
