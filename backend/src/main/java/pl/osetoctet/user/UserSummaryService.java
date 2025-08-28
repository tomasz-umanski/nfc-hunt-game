package pl.osetoctet.user;

import pl.osetoctet.user.model.dto.UserSummaryResponseDto;

import java.util.List;

public interface UserSummaryService {

    List<UserSummaryResponseDto> getAll();

}
