package dev.saha.otpauthserverweb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AndroidClientResponse {
    private String serialNumber;
    private List<String>tokens;

    public AndroidClientResponse() {
        this.tokens = new ArrayList<>();
    }
}
