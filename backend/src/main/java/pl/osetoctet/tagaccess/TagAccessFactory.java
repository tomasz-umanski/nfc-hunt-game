package pl.osetoctet.tagaccess;

import org.springframework.stereotype.Component;
import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDetailsDto;
import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDto;
import pl.osetoctet.tagaccess.model.entity.TagAccess;
import pl.osetoctet.taglocation.model.entity.TagLocation;
import pl.osetoctet.user.model.entity.User;

@Component
class TagAccessFactory {

    public static TagAccessResponseDto createTagAccessResponseDetailsDto(TagLocation tagLocation,  boolean isUnlocked) {
        TagAccessResponseDto.TagAccessResponseDtoBuilder builder = TagAccessResponseDto.builder()
                .name(tagLocation.getName())
                .longitude(tagLocation.getLongitude())
                .latitude(tagLocation.getLatitude())
                .isUnlocked(isUnlocked);

        if (isUnlocked) {
            TagAccessResponseDetailsDto tagAccessResponseDetailsDto = TagAccessResponseDetailsDto.builder()
                    .description(tagLocation.getDescription())
                    .build();

            builder
                    .tagAccessResponseDetails(tagAccessResponseDetailsDto);
        }


        return builder.build();
    }

    public static TagAccess createTagAccessFromUserAndTagLocation(User user, TagLocation tagLocation) {
        return TagAccess.builder()
                .user(user)
                .tagLocation(tagLocation)
                .build();
    }

}
