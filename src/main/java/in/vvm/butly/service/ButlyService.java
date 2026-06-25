package in.vvm.butly.service;

import org.springframework.stereotype.Service;

import in.vvm.butly.model.ShortenRequest;
import in.vvm.butly.model.ShortenResponse;

@Service
public interface ButlyService {

    ShortenResponse shortenUrl(ShortenRequest request);

    String resolveShortUrl(String shortUrl);
}
