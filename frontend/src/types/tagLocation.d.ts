export interface CreateTagLocationUnlockImageDto {
    imageFilename: string;
    displayOrder: number;
}

export interface CreateTagLocationDto {
    name: string;
    description?: string;
    nfcTagUuid: string;
    latitude: number;
    longitude: number;
    lockedImageFilename: string;
    unlockedImageFilename: string;
    unlockImages: CreateTagLocationUnlockImageDto[];
}

export interface UpdateTagLocationDto {
    name?: string;
    description?: string;
    nfcTagUuid?: string;
    latitude?: number;
    longitude?: number;
    lockedImageFilename?: string;
    unlockedImageFilename?: string;
    unlockImages?: CreateTagLocationUnlockImageDto[];
}

export interface TagLocationUnlockImageResponseDto {
    imageFilename: string;
    displayOrder: number;
}

export interface TagLocationResponseDto {
    id: string;
    name: string;
    nfcTagUuid: string;
    description?: string;
    latitude: number;
    longitude: number;
    lockedImageFilename: string;
    unlockedImageFilename: string;
    unlockImages: TagLocationUnlockImageResponseDto[];
}
