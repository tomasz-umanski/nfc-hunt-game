package pl.osetoctet.taglocation;

import org.springframework.stereotype.Component;
import pl.osetoctet.taglocation.model.dto.CreateTagLocationDto;
import pl.osetoctet.taglocation.model.dto.TagLocationResponseDto;
import pl.osetoctet.taglocation.model.entity.TagLocation;

@Component
class TagLocationFactory {

    public static TagLocation createFromCreateTagLocationDto(CreateTagLocationDto createTagLocationDto) {
        return TagLocation.builder()
                .name(createTagLocationDto.getName())
                .description(createTagLocationDto.getDescription())
                .nfcTagUuid(createTagLocationDto.getNfcTagUuid())
                .latitude(createTagLocationDto.getLatitude())
                .longitude(createTagLocationDto.getLongitude())
                .build();
    }

    public static TagLocationResponseDto createFromTagLocation(TagLocation tagLocation) {
        return TagLocationResponseDto.builder()
                .id(tagLocation.getId().toString())
                .name(tagLocation.getName())
                .description(tagLocation.getDescription())
                .latitude(tagLocation.getLatitude())
                .longitude(tagLocation.getLongitude())
                .build();
    }

}
