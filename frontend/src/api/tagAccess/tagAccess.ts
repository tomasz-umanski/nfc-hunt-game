import apiClient from "@/api/apiClient.ts";
import type {
    TagAccessResponseDto,
    UnlockTagRequestDto
} from "@/types/tagAccess";

export const getAllTagsPublic = async (): Promise<TagAccessResponseDto[]> => {
    const response = await apiClient.get<TagAccessResponseDto[]>(
        '/v1/tag-access/public'
    );
    return response.data;
};

export const getAllTagsWithAccessRequest = async (): Promise<TagAccessResponseDto[]> => {
    const response = await apiClient.get<TagAccessResponseDto[]>(
        '/v1/tag-access/user'
    );
    return response.data;
};

export const getByTagLocationId = async (tagLocationId: String): Promise<TagAccessResponseDto> => {
    const response = await apiClient.get<TagAccessResponseDto>(
        `/v1/tag-access/${tagLocationId}`,
    );
    return response.data;
};

export const unlockTagRequest = async (data: UnlockTagRequestDto): Promise<TagAccessResponseDto> => {
    const response = await apiClient.post<TagAccessResponseDto>(
        '/v1/tag-access/unlock',
        data
    );
    return response.data;
};
