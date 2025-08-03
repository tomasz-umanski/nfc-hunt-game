import type {AuthenticateUserDto, AuthenticationResponse, RefreshTokenOperationsDto} from "@/types/auth";
import apiClient from "@/api/apiClient.ts";

export const loginRequest = async (
    data: AuthenticateUserDto
): Promise<AuthenticationResponse> => {
    const response = await apiClient.post<AuthenticationResponse>(
        '/v1/auth/authenticate',
        data
    );

    return response.data;
};

export const logoutRequest = async (data: RefreshTokenOperationsDto): Promise<void> => {
    await apiClient.post(
        "/v1/auth/logout",
        data
    );
};
