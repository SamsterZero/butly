package in.vvm.butly.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vvm.butly.entity.Butly;
import in.vvm.butly.handler.ShortUrlNotFoundException;
import in.vvm.butly.model.ShortenRequest;
import in.vvm.butly.model.ShortenResponse;
import in.vvm.butly.repository.ButlyRepository;
import in.vvm.butly.util.Base62;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ButlyServiceImpl implements ButlyService {

    private final StringRedisTemplate redisTemplate;
    private final ButlyRepository butlyRepository;

    private static final Duration CACHE_TTL = Duration.ofHours(24);

    @Transactional
    @Override
    public ShortenResponse shortenUrl(ShortenRequest request) {

        Butly butly = new Butly();

        butly.setLongUrl(request.getLongUrl());

        butly = butlyRepository.save(butly);

        String shortUrl = Base62.encode(butly.getId());

        butly.setShortUrl(shortUrl);

        butly = butlyRepository.save(butly);

        String cacheKey = buildKey(shortUrl);
        redisTemplate.opsForValue().set(
                cacheKey,
                butly.getLongUrl(),
                CACHE_TTL);

        ShortenResponse res = new ShortenResponse();
        res.setShortUrl(shortUrl);
        return res;
    }

    @Transactional(readOnly = true)
    @Override
    public String resolveShortUrl(String shortUrl) {
        String cacheKey = buildKey(shortUrl);

        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);

        if (cachedUrl != null)
            return cachedUrl;

        Butly butly = butlyRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortUrl));

        redisTemplate.opsForValue().set(
                cacheKey,
                butly.getLongUrl(),
                CACHE_TTL);

        return butly.getLongUrl();
    }

    private String buildKey(String shortUrl) {
        return "shortUrl:" + shortUrl;
    }
}