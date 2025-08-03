import axios from 'axios';
import {useAuthStore} from "@/store/authStore.ts";
import {getDeviceId, getLanguageHeader} from "@/utils/authUtils.ts";
import config from "@/config";

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

export default apiClient;
