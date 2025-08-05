import type {
    AuthenticationResponse,
} from "@/types/auth";
import apiClient from "@/api/apiClient.ts";
import type {ChangePasswordDto} from "@/types/profile";

export const changePasswordRequest = async (
    data: ChangePasswordDto
): Promise<void> => {
    await apiClient.post<AuthenticationResponse>(
        '/v1/user/change-password',
        data
    );
};

export const deleteAccountRequest = async (): Promise<void> => {
    await apiClient.delete(
        "/v1/user/deactivate-account"
    );
};
