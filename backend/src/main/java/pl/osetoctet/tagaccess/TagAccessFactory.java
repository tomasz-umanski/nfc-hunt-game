package pl.osetoctet.tagaccess;

import org.springframework.stereotype.Component;
import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDetailsDto;
import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDto;
import pl.osetoctet.tagaccess.model.dto.TagAccessUnlockImageResponseDto;
import pl.osetoctet.tagaccess.model.entity.TagAccess;
import pl.osetoctet.taglocation.model.entity.TagLocation;
import pl.osetoctet.taglocation.model.entity.TagLocationUnlockImage;
import pl.osetoctet.user.model.entity.User;

import java.util.List;

@Component
class TagAccessFactory {

    public static TagAccessResponseDto createTagAccessResponseDetailsDto(TagLocation tagLocation, boolean isUnlocked) {
        TagAccessResponseDto.TagAccessResponseDtoBuilder builder = TagAccessResponseDto.builder()
                .longitude(tagLocation.getLongitude())
                .latitude(tagLocation.getLatitude())
                .isUnlocked(isUnlocked)
                .lockedImageFilename(tagLocation.getLockedImageFilename());

        if (isUnlocked) {
            List<TagAccessUnlockImageResponseDto> unlockImages = tagLocation.getUnlockImages().stream()
                    .map(TagAccessFactory::createFromUnlockImageEntity)
                    .toList();

            TagAccessResponseDetailsDto tagAccessResponseDetailsDto = TagAccessResponseDetailsDto.builder()
                    .name(tagLocation.getName())
                    .description(tagLocation.getDescription())
                    .unlockedImageFilename(tagLocation.getUnlockedImageFilename())
                    .unlockImages(unlockImages)
                    .build();

            builder
                    .tagAccessResponseDetails(tagAccessResponseDetailsDto);
        }

        return builder.build();
    }

    private static TagAccessUnlockImageResponseDto createFromUnlockImageEntity(TagLocationUnlockImage tagLocationUnlockImage) {
        return TagAccessUnlockImageResponseDto.builder()
                .imageFilename(tagLocationUnlockImage.getImageFilename())
                .displayOrder(tagLocationUnlockImage.getDisplayOrder())
                .build();
    }

    public static TagAccess createTagAccessFromUserAndTagLocation(User user, TagLocation tagLocation) {
        return TagAccess.builder()
                .user(user)
                .tagLocation(tagLocation)
                .longitude(tagLocation.getLongitude())
                .latitude(tagLocation.getLatitude())
                .build();
    }

}
