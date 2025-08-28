import type {
    AuthenticateUserDto,
    AuthenticationResponse,
    RefreshTokenOperationsDto,
    RegisterUserDto
} from "@/types/auth";
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

export const registerRequest = async (data: RegisterUserDto): Promise<AuthenticationResponse> => {
    const response = await apiClient.post<AuthenticationResponse>(
        '/v1/auth/register',
        data
    );
    return response.data;
};

export const refreshTokenRequest = async (data: RefreshTokenOperationsDto): Promise<AuthenticationResponse> => {
    const response = await apiClient.post<AuthenticationResponse>(
        '/v1/auth/refresh-token',
        data
    );
    return response.data;
};
