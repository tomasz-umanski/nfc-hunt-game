import apiClient from "@/api/apiClient.ts";
import type {UserSummary} from "@/types/userSummary";

export const getUserSummaryRequest = async (): Promise<UserSummary[]> => {
    const response = await apiClient.get<UserSummary[]>(
        '/v1/user-summary'
    );
    return response.data;
};
