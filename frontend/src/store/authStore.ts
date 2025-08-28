import {create} from 'zustand';
import {decodeToken} from '@/utils/authUtils';
import type {AuthenticationResponse, DecodedToken} from '@/types/auth';

interface AuthState {
    user: DecodedToken | null;
    accessToken: string | null;
    refreshToken: string | null;
    login: (auth: AuthenticationResponse) => void;
    logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
    user: null,
    accessToken: null,
    refreshToken: null,
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
}));
