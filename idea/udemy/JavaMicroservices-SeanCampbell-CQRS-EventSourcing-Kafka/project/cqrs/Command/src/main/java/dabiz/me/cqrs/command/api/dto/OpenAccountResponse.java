package dabiz.me.cqrs.command.api.dto;

import dabiz.me.cqrs.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccountResponse extends BaseResponse {
    private String id;
    public OpenAccountResponse(String message, String id){
        super(message);
        this.id=id;
    }
}
