package pl.osetoctet.tagaccess;

import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDto;
import pl.osetoctet.tagaccess.model.dto.UnlockTagRequestDto;
import pl.osetoctet.user.model.entity.User;

import java.util.List;

public interface TagAccessService {

    TagAccessResponseDto unlock(User user, UnlockTagRequestDto unlockTagRequestDto);

    List<TagAccessResponseDto> getAllTagsForUser(User user);

    List<TagAccessResponseDto> getAllTags();

}
