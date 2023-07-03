package id.codecorn.bpjsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BpjsResponse<T> {
    private T response;
    private MetaData metaData;
}
