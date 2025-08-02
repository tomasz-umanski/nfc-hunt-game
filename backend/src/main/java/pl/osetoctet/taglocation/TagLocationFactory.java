package pl.osetoctet.taglocation;

import org.springframework.stereotype.Component;
import pl.osetoctet.taglocation.model.dto.CreateTagLocationDto;
import pl.osetoctet.taglocation.model.dto.CreateTagLocationUnlockImageDto;
import pl.osetoctet.taglocation.model.dto.TagLocationResponseDto;
import pl.osetoctet.taglocation.model.dto.TagLocationUnlockImageResponseDto;
import pl.osetoctet.taglocation.model.entity.TagLocation;
import pl.osetoctet.taglocation.model.entity.TagLocationUnlockImage;

import java.util.List;

@Component
class TagLocationFactory {

    public static TagLocation createFromCreateTagLocationDto(CreateTagLocationDto createTagLocationDto) {
        TagLocation tagLocation = TagLocation.builder()
                .name(createTagLocationDto.getName())
                .description(createTagLocationDto.getDescription())
                .nfcTagUuid(createTagLocationDto.getNfcTagUuid())
                .latitude(createTagLocationDto.getLatitude())
                .longitude(createTagLocationDto.getLongitude())
                .lockedImageFilename(createTagLocationDto.getLockedImageFilename())
                .unlockedImageFilename(createTagLocationDto.getUnlockedImageFilename())
                .build();

        List<TagLocationUnlockImage> unlockImages = createTagLocationDto.getUnlockImages().stream()
                .map(unlockImageDto -> createUnlockImageFromDto(unlockImageDto, tagLocation))
                .toList();

        tagLocation.setUnlockImages(unlockImages);

        return tagLocation;
    }

    public static TagLocationUnlockImage createUnlockImageFromDto(CreateTagLocationUnlockImageDto createTagLocationUnlockImageDto, TagLocation tagLocation) {
        return TagLocationUnlockImage.builder()
                .imageFilename(createTagLocationUnlockImageDto.getImageFilename())
                .displayOrder(createTagLocationUnlockImageDto.getDisplayOrder())
                .tagLocation(tagLocation)
                .build();
    }

    public static TagLocationResponseDto createFromTagLocation(TagLocation tagLocation) {
        List<TagLocationUnlockImageResponseDto> unlockImagesDto = tagLocation.getUnlockImages().stream()
                .map(TagLocationFactory::createFromUnlockImageEntity)
                .toList();

        return TagLocationResponseDto.builder()
                .id(tagLocation.getId().toString())
                .name(tagLocation.getName())
                .description(tagLocation.getDescription())
                .latitude(tagLocation.getLatitude())
                .longitude(tagLocation.getLongitude())
                .lockedImageFilename(tagLocation.getLockedImageFilename())
                .unlockedImageFilename(tagLocation.getUnlockedImageFilename())
                .unlockImages(unlockImagesDto)
                .build();
    }

    private static TagLocationUnlockImageResponseDto createFromUnlockImageEntity(TagLocationUnlockImage tagLocationUnlockImage) {
        return TagLocationUnlockImageResponseDto.builder()
                .imageFilename(tagLocationUnlockImage.getImageFilename())
                .displayOrder(tagLocationUnlockImage.getDisplayOrder())
                .build();
    }

}
