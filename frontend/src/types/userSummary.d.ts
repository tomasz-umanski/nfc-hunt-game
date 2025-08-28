export interface UserSummary {
    email: string;
    accessedTagsCount: number;
    createdAt: string;
    userAccessedTagDtoList: UserAccessedTagDto[];
}

export interface UserAccessedTagDto {
    name: string;
    latitude: number;
    longitude: number;
    unlockedAt: string;
}
