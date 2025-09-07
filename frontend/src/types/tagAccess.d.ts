export interface TagAccessUnlockImageResponseDto {
    imageFilename: string;
    displayOrder: number;
}

export interface TagAccessResponseDetailsDto {
    name: string;
    description: string;
    unlockedImageFilename: string;
    unlockImages: TagAccessUnlockImageResponseDto[];
}

export interface TagAccessResponseDto {
    tagLocationId: string;
    latitude: number;
    longitude: number;
    lockedImageFilename: string;
    tagAccessResponseDetails?: TagAccessResponseDetailsDto;
    unlocked: boolean;
}

export interface UnlockTagRequestDto {
    nfcTagUuid: string;
    latitude: number;
    longitude: number;
}
