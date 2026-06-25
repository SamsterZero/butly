package in.vvm.butly.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShortenResponse {

    @NotBlank
    private String shortUrl;
}