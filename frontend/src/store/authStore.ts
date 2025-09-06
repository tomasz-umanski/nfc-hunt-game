import {create} from 'zustand';
import {decodeToken, isTokenExpired} from '@/utils/authUtils';
import type {AuthenticationResponse, DecodedToken} from '@/types/auth';
import {refreshTokenRequest} from "@/api/auth/auth.ts";

interface AuthState {
    user: DecodedToken | null;
    accessToken: string | null;
    refreshToken: string | null;
    login: (auth: AuthenticationResponse) => void;
    logout: () => void;
    hydrate: () => Promise<void>;
}

const getInitialState = (): Omit<AuthState, 'login' | 'logout' | 'hydrate'> => {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    const user = accessToken && !isTokenExpired(accessToken) ? decodeToken(accessToken) : null;

    return {
        user,
        accessToken: user ? accessToken : null,
        refreshToken,
    };
};

export const useAuthStore = create<AuthState>((set, get) => ({
    ...getInitialState(),

    login: (auth) => {
        const decoded = decodeToken(auth.accessToken);
        set({
            user: decoded,
            accessToken: auth.accessToken,
            refreshToken: auth.refreshToken,
        });
        localStorage.setItem('accessToken', auth.accessToken);
        localStorage.setItem('refreshToken', auth.refreshToken);
    },

    logout: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        set({user: null, accessToken: null, refreshToken: null});
    },

    hydrate: async () => {
        const {accessToken, refreshToken} = get();

        if (accessToken && !isTokenExpired(accessToken)) {
            set({user: decodeToken(accessToken)});
        } else if (refreshToken) {
            try {
                const response = await refreshTokenRequest({refreshToken: refreshToken});
                const decoded = decodeToken(response.accessToken);
                set({
                    user: decoded,
                    accessToken: response.accessToken,
                    refreshToken: response.refreshToken || refreshToken, // in case refresh token rotates
                });
                localStorage.setItem('accessToken', response.accessToken);
                if (response.refreshToken) {
                    localStorage.setItem('refreshToken', response.refreshToken);
                }
            } catch (error) {
                console.error('Token refresh failed:', error);
                get().logout();
            }
        } else {
            // No valid tokens, logout
            get().logout();
        }
    },
}));