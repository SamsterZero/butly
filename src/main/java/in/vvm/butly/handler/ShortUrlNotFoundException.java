package in.vvm.butly.handler;

public class ShortUrlNotFoundException extends RuntimeException {

    public ShortUrlNotFoundException(String shortUrl){
        super("Short URL not found: " + shortUrl);
    }

}
