export interface LoginRequest{
  email: string;
  password: string;
}

export interface AuthResponse{
  accessToken: string;
  refreshToken: string;
}

export interface GoogleLoginRequest{
  idToken: string;
}
