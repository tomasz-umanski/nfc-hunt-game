package pl.osetoctet.taglocation;

import pl.osetoctet.taglocation.model.dto.CreateTagLocationDto;
import pl.osetoctet.taglocation.model.dto.TagLocationResponseDto;
import pl.osetoctet.taglocation.model.dto.UpdateTagLocationDto;

import java.util.List;
import java.util.UUID;

public interface TagLocationService {

    TagLocationResponseDto create(CreateTagLocationDto createTagLocationDto);

    List<TagLocationResponseDto> getAll();

    TagLocationResponseDto updateById(UUID id, UpdateTagLocationDto updateTagLocationDto);

    void deleteById(UUID id);

}
