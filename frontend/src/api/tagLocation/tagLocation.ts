import apiClient from "@/api/apiClient.ts";
import type {
    TagLocationResponseDto,
    CreateTagLocationDto,
    UpdateTagLocationDto
} from "@/types/tagLocation";

export const getAllTagLocationsRequest = async (): Promise<TagLocationResponseDto[]> => {
    const response = await apiClient.get<TagLocationResponseDto[]>(
        '/v1/tag-location'
    );
    return response.data;
};

export const createTagLocationRequest = async (data: CreateTagLocationDto): Promise<TagLocationResponseDto> => {
    const response = await apiClient.post<TagLocationResponseDto>(
        '/v1/tag-location',
        data
    );
    return response.data;
};

export const updateTagLocationRequest = async (id: string, data: UpdateTagLocationDto): Promise<TagLocationResponseDto> => {
    const response = await apiClient.patch<TagLocationResponseDto>(
        `/v1/tag-location/${id}`,
        data
    );
    return response.data;
};

export const deleteTagLocationRequest = async (id: string): Promise<void> => {
    await apiClient.delete(`/v1/tag-location/${id}`);
};
