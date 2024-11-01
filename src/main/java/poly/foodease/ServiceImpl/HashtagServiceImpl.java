package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.HashtagMapper;
import poly.foodease.Model.Entity.Hashtag;
import poly.foodease.Model.Request.HashtagRequest;
import poly.foodease.Model.Response.HashtagResponse;
import poly.foodease.Repository.HashtagRepo;
import poly.foodease.Service.HashtagService;

@Service
public class HashtagServiceImpl implements HashtagService {

    @Autowired
    private HashtagRepo hashtagRepo;

    @Autowired
    private HashtagMapper hashtagMapper;

    @Override
    public HashtagResponse createHashtag(HashtagRequest hashtagRequest) {
        Hashtag newHashtag = hashtagMapper.convertReqToEn(hashtagRequest);
        hashtagRepo.save(newHashtag);
        return hashtagMapper.convertEnToRes(newHashtag);
    }

    @Override
    public HashtagResponse getHashtagById(Integer hashtagId) {
        Hashtag hashtag = hashtagRepo.findById(hashtagId)
                .orElseThrow(() -> new EntityNotFoundException("Hashtag not found"));
        return hashtagMapper.convertEnToRes(hashtag);
    }

    @Override
    public void deleteHashtag(Integer hashtagId) {
        Hashtag hashtag = hashtagRepo.findById(hashtagId)
                .orElseThrow(() -> new EntityNotFoundException("Hashtag not found"));
        hashtagRepo.delete(hashtag);
    }

    @Override
    public List<HashtagResponse> getHashtags() {
        return hashtagRepo.findAll().stream()
                .map(hashtagMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}
