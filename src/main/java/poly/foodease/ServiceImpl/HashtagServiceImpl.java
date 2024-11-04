package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Optional;
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
        // Kiểm tra sự tồn tại của hashtag theo tên
        Hashtag existingHashtag = hashtagRepo.findByHashtagName(hashtagRequest.getHashtagName()).orElse(null);

        if (existingHashtag != null) {
            // Trả về Hashtag nếu đã tồn tại
            return hashtagMapper.convertEnToRes(existingHashtag);
        }

        // Tạo mới nếu chưa tồn tại
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

    @Override
    public HashtagResponse findByHashtagName(String hashtagName) {
        // Tìm kiếm hashtag bằng tên
        Optional<Hashtag> optionalHashtag = hashtagRepo.findByHashtagName(hashtagName);
        
        // Nếu tồn tại, chuyển đổi và trả về
        if (optionalHashtag.isPresent()) {
            return hashtagMapper.convertEnToRes(optionalHashtag.get());
        }
        
        // Nếu không tồn tại, trả về null hoặc một giá trị mặc định
        return null; // hoặc có thể trả về một giá trị khác nếu bạn muốn
    }
    
}
