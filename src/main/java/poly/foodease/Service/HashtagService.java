package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Request.HashtagRequest;
import poly.foodease.Model.Response.HashtagResponse;

public interface HashtagService {

    HashtagResponse createHashtag(HashtagRequest hashtagRequest);

    HashtagResponse getHashtagById(Integer hashtagId);

    void deleteHashtag(Integer hashtagId);

    List<HashtagResponse> getHashtags();

}
