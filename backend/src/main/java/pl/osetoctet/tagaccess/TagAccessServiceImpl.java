package pl.osetoctet.tagaccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.osetoctet.common.exception.RetrieveException;
import pl.osetoctet.common.exception.SaveException;
import pl.osetoctet.common.exception.ValidationException;
import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDto;
import pl.osetoctet.tagaccess.model.dto.UnlockTagRequestDto;
import pl.osetoctet.tagaccess.model.entity.TagAccess;
import pl.osetoctet.taglocation.TagLocationEntityService;
import pl.osetoctet.taglocation.model.entity.TagLocation;
import pl.osetoctet.user.model.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class TagAccessServiceImpl implements TagAccessService {

    private final TagAccessRepository tagAccessRepository;
    private final TagLocationEntityService tagLocationEntityService;

    private static final double MAX_DISTANCE_METERS = 50.0;
    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    @Transactional
    public void unlock(User user, UnlockTagRequestDto unlockTagRequestDto) {
        log.info("Attempting to unlock tag {} for user {}", unlockTagRequestDto.getNfcTagUuid(), user.getId());

        try {
            TagLocation tagLocation = tagLocationEntityService.findByNfcTagId(unlockTagRequestDto.getNfcTagUuid())
                    .orElseThrow(() -> new ValidationException("validation.tag.notFound"));

            boolean hasUserAlreadyUnlockedTag = tagAccessRepository.existsByUserIdAndTagLocationId(user.getId(), tagLocation.getId());
            if (hasUserAlreadyUnlockedTag) {
                throw new ValidationException("validation.tag.alreadyUnlocked");
            }

            boolean isUserTooFarFromTag = isUserTooFarFromTag(tagLocation, unlockTagRequestDto);
            if (isUserTooFarFromTag) {
                throw new ValidationException("validation.location.tooFarFromTag");
            }

            TagAccess tagAccessToSave = TagAccessFactory.createTagAccessFromUserAndTagLocation(user, tagLocation);
            TagAccess savedAccess = tagAccessRepository.save(tagAccessToSave);
            log.info("Successfully unlocked tag {} for user {} with access id {}",
                    tagLocation.getNfcTagUuid(), user.getId(), savedAccess.getId());
        } catch (ValidationException e) {
            log.warn("Validation failed while unlocking tag {} for user {}: {}",
                    unlockTagRequestDto.getNfcTagUuid(), user.getId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to unlock tag {} for user {}", unlockTagRequestDto.getNfcTagUuid(), user.getId(), e);
            throw new SaveException("Failed to unlock tag", e);
        }
    }

    private boolean isUserTooFarFromTag(TagLocation tagLocation, UnlockTagRequestDto unlockTagRequestDto) {
        double distance = calculateDistance(
                tagLocation.getLatitude(), tagLocation.getLongitude(),
                unlockTagRequestDto.getLatitude(), unlockTagRequestDto.getLongitude()
        );
        return distance > MAX_DISTANCE_METERS;
    }

    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c * 1000;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagAccessResponseDto> getAllTagsForUser(User user) {
        try {
            List<TagLocation> allTags = tagLocationEntityService.findAll();

            Set<UUID> unlockedTagIds = tagAccessRepository
                    .findByUserId(user.getId())
                    .stream()
                    .map(access -> access.getTagLocation().getId())
                    .collect(Collectors.toSet());

            return allTags.stream()
                    .map(tag -> TagAccessFactory.createTagAccessResponseDetailsDto(tag, unlockedTagIds.contains(tag.getId())))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to retrieve tags for user {}", user.getId(), e);
            throw new RetrieveException("Failed to retrieve tags", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagAccessResponseDto> getAllTags() {
        try {
            List<TagLocation> allTags = tagLocationEntityService.findAll();
            return allTags.stream()
                    .map(tag -> TagAccessFactory.createTagAccessResponseDetailsDto(tag, false))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve tags", e);
            throw new RetrieveException("Failed to retrieve tags", e);
        }
    }

}
