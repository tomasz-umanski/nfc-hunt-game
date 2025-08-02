package pl.osetoctet.tagaccess;

import java.util.UUID;

public interface TagAccessEntityService {

    boolean isAccessedByAnyUser(UUID tagLocationId);

}
