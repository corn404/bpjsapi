package id.codecorn.bpjsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AesKeySpec {
    private SecretKeySpec key;
    private IvParameterSpec iv;
}
