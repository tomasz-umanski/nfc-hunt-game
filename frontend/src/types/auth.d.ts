export interface AuthenticateUserDto {
    email: string;
    password: string;
}

export interface AuthenticationResponse {
    accessToken: string;
    refreshToken: string;
}

export interface DecodedToken {
    sub: string;
    role: 'ADMIN' | 'USER';
    type: 'access_token';
    deviceId: string;
    iat: number;
    exp: number;
    jti: string;
}

export interface RefreshTokenOperationsDto {
    refreshToken: string;
}

export interface RegisterUserDto {
    email: string;
    password: string;
    confirmPassword: string;
    acceptGameRules: boolean;
    acceptPrivacyPolicy: boolean;
    confirmAge: boolean;
}