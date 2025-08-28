package pl.osetoctet.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "Data Transfer Object for providing user summary details")
public class UserSummaryResponseDto {

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "List of user's accessed tags")
    List<UserAccessedTagDto> userAccessedTagDtoList;

    @Schema(description = "Count of user's accessed tags")
    int accessedTagsCount;

    @Schema(description = "User's registration date", example = "2024-05-19T12:00:00Z")
    private OffsetDateTime createdAt;

}
