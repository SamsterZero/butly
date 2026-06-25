package in.vvm.butly.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShortenRequest {

    @NotBlank
    private String longUrl;
}
