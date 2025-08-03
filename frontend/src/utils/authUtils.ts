import {jwtDecode} from 'jwt-decode';
import type {DecodedToken} from "@/types/auth";
import {v4} from 'uuid';
import {useAuthStore} from "@/store/authStore.ts";

export const decodeToken = (token: string): DecodedToken => jwtDecode(token);

export const getDeviceId = (): string => {
    const localKey = 'device-id';
    let id = localStorage.getItem(localKey);

    if (!id) {
        id = v4();
        localStorage.setItem(localKey, id);
    }
    return id;
};

export const getLanguageHeader = (): string => {
    const storedLang = localStorage.getItem('lang');
    const preferred = storedLang || navigator.languages?.[0] || navigator.language || 'en-US';

    const normalizeLang = (lang: string): string => {
        if (lang.toLowerCase() === 'en') return 'en-US';
        if (lang.toLowerCase() === 'pl') return 'pl-PL';
        return lang;
    };

    return normalizeLang(preferred);
};

export const isAuthenticated = (): boolean => {
    const {accessToken} = useAuthStore.getState();
    return accessToken != null;
};

export const isTokenExpired = (token: string): boolean => {
    try {
        const decoded: DecodedToken = decodeToken(token);
        const now = Date.now() / 1000;
        return decoded.exp < now;
    } catch {
        return true;
    }
};
