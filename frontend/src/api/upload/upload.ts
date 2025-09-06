import apiClient from "@/api/apiClient.ts";
import type {UploadResponse} from "@/types/upload";

export const uploadFileRequest = async (file: File): Promise<UploadResponse> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await apiClient.post<UploadResponse>(
        '/v1/upload',
        formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        }
    );
    return response.data;
};

export const getImageUrl = (filename: string): string => {
    const baseUrl = apiClient.defaults.baseURL?.replace('/api', '') || '';
    return `${baseUrl}/public/uploads/${filename}`;
};