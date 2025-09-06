import {jwtDecode} from 'jwt-decode';
import type {DecodedToken} from "@/types/auth";
import {v4} from 'uuid';
import type {RoleBasedRouteProps} from "@/routes/RoleBasedRoute.tsx";

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
    const accessToken = localStorage.getItem('accessToken');
    return accessToken != null;
};

export const hasAdminRole = (): boolean => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken == null)
        return false;

    const user = decodeToken(accessToken);
    return user.role === "ADMIN";
};

export const hasAllowedRole = ({allowedRoles}: RoleBasedRouteProps): boolean => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken == null)
        return false;

    const user = decodeToken(accessToken);
    return allowedRoles.includes(user.role);
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
