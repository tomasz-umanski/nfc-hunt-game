import axios from 'axios';
import {useAuthStore} from "@/store/authStore.ts";
import {getDeviceId, getLanguageHeader} from "@/utils/authUtils.ts";
import config from "@/config";
import type {AuthenticationResponse, RefreshTokenOperationsDto} from "@/types/auth";

let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (error: any, token: string | null = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

const apiClient = axios.create({
    baseURL: config.apiUrl + "/api",
    headers: {
        'Content-Type': 'application/json',
    }
});

apiClient.interceptors.request.use((config) => {
    const {accessToken} = useAuthStore.getState();

    if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    config.headers['Device-Id'] = getDeviceId();
    config.headers['Accept-Language'] = getLanguageHeader();

    return config;
});

apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            const {refreshToken, logout, login} = useAuthStore.getState();

            if (!refreshToken) {
                logout();
                return Promise.reject(error);
            }

            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({resolve, reject});
                }).then(token => {
                    originalRequest.headers['Authorization'] = 'Bearer ' + token;
                    return apiClient(originalRequest);
                }).catch(err => {
                    return Promise.reject(err);
                });
            }

            isRefreshing = true;

            try {
                const body: RefreshTokenOperationsDto = {refreshToken};
                const response = await axios.post<AuthenticationResponse>(
                    config.apiUrl + '/api/v1/auth/refresh-token',
                    body,
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            'Device-Id': getDeviceId(),
                            'Accept-Language': getLanguageHeader(),
                        }
                    }
                );

                const authResponse: AuthenticationResponse = response.data;

                login({
                    accessToken: authResponse.accessToken,
                    refreshToken: authResponse.refreshToken,
                });
                processQueue(null, authResponse.accessToken);

                originalRequest.headers['Authorization'] = `Bearer ${authResponse.accessToken}`;
                if (originalRequest.data && typeof originalRequest.data === 'string') {
                    try {
                        const parsedData = JSON.parse(originalRequest.data);
                        if ('refreshToken' in parsedData) {
                            parsedData.refreshToken = authResponse.refreshToken;
                            originalRequest.data = JSON.stringify(parsedData);
                        }
                    } catch (e) {
                    }
                }

                return apiClient(originalRequest);

            } catch (err) {
                processQueue(err, null);
                logout();
                return Promise.reject(err);
            } finally {
                isRefreshing = false;
            }
        }

        return Promise.reject(error);
    }
);

export default apiClient;
