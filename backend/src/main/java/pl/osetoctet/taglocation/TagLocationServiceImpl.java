package pl.osetoctet.taglocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.osetoctet.common.exception.SaveException;
import pl.osetoctet.common.exception.ValidationException;
import pl.osetoctet.taglocation.model.dto.CreateTagLocationDto;
import pl.osetoctet.taglocation.model.dto.TagLocationResponseDto;
import pl.osetoctet.taglocation.model.dto.UpdateTagLocationDto;
import pl.osetoctet.taglocation.model.entity.TagLocation;
import pl.osetoctet.usertagaccess.UserTagAccessEntityService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class TagLocationServiceImpl implements TagLocationService {

    private final TagLocationRepository tagLocationRepository;
    private final UserTagAccessEntityService userTagAccessEntityService;

    @Override
    @Transactional()
    public TagLocationResponseDto create(CreateTagLocationDto createTagLocationDto) {
        if (tagLocationRepository.existsByNfcTagUuid(createTagLocationDto.getNfcTagUuid())) {
            throw new ValidationException("validation.tagLocation.alreadyExists");
        }

        try {
            TagLocation tagLocationToSave = TagLocationFactory.createFromCreateTagLocationDto(createTagLocationDto);
            TagLocation savedTagLocation = tagLocationRepository.save(tagLocationToSave);
            log.info("Saved new tag location with id = {}", savedTagLocation.getId());
            return TagLocationFactory.createFromTagLocation(savedTagLocation);
        } catch (Exception e) {
            log.error("Failed to create tag location with name: {}", createTagLocationDto.getName(), e);
            throw new SaveException("Failed to create tag location", e);
        }
    }

    @Override
    public List<TagLocationResponseDto> getAll() {
        return tagLocationRepository.findAllByOrderByNameAsc().stream()
                .map(TagLocationFactory::createFromTagLocation)
                .toList();
    }

    @Override
    public TagLocationResponseDto updateById(UUID id, UpdateTagLocationDto updateTagLocationDto) {
        Optional<TagLocation> optionalTagLocation = tagLocationRepository.findActiveById(id);

        if (optionalTagLocation.isEmpty()) {
            throw new ValidationException("validation.tagLocation.notFound");
        }

        TagLocation existingTagLocation = optionalTagLocation.get();

        if (updateTagLocationDto.getNfcTagUuid() != null &&
                !updateTagLocationDto.getNfcTagUuid().equals(existingTagLocation.getNfcTagUuid())) {
            if (tagLocationRepository.existsByNfcTagUuid(updateTagLocationDto.getNfcTagUuid())) {
                throw new ValidationException("validation.tagLocation.alreadyExists");
            }
        }

        try {
            updateEntityFields(existingTagLocation, updateTagLocationDto);
            TagLocation updatedTagLocation = tagLocationRepository.save(existingTagLocation);
            log.info("Updated tag location with id: {}", id);
            return TagLocationFactory.createFromTagLocation(updatedTagLocation);
        } catch (Exception e) {
            log.error("Failed to update tag location with id: {}", id, e);
            throw new SaveException("Failed to update tag location", e);
        }
    }

    private void updateEntityFields(TagLocation existingTagLocation, UpdateTagLocationDto updateDto) {
        if (updateDto.getName() != null) {
            existingTagLocation.setName(updateDto.getName());
        }
        if (updateDto.getDescription() != null) {
            existingTagLocation.setDescription(updateDto.getDescription());
        }
        if (updateDto.getNfcTagUuid() != null) {
            existingTagLocation.setNfcTagUuid(updateDto.getNfcTagUuid());
        }
        if (updateDto.getLatitude() != null) {
            existingTagLocation.setLatitude(updateDto.getLatitude());
        }
        if (updateDto.getLongitude() != null) {
            existingTagLocation.setLongitude(updateDto.getLongitude());
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Optional<TagLocation> optionalTagLocation = tagLocationRepository.findActiveById(id);
        if (optionalTagLocation.isEmpty()) {
            throw new ValidationException("Tag location no longer exists");
        }
        if (userTagAccessEntityService.isAccessedByAnyUser(id)) {
            throw new ValidationException("Tag location is referenced by user access records and cannot be modified");
        }
        try {
            TagLocation tagLocationToDelete = optionalTagLocation.get();
            tagLocationRepository.delete(tagLocationToDelete);
            log.info("Deleted tag location with id = {}", id);
        } catch (Exception e) {
            log.error("Failed to delete tag location with id: {}", id, e);
            throw new SaveException("Failed to delete tag location", e);
        }
    }

}
