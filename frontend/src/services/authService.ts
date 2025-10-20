import api from './api';
import { AuthResponseDTO, LoginDTO, RegisterDTO, ChangePasswordDTO } from '../types/auth';
import { UserDTO } from '../types/user'

export const authService = {
    login: async (credentials: LoginDTO): Promise<AuthResponseDTO> => {
        const response = await api.post('/auth/login', credentials);
        return response.data;
    },

    register: async (data: RegisterDTO): Promise<AuthResponseDTO> => {
        const response = await api.post('/auth/register', data);
        return response.data;
    },

    refreshToken: async (refreshToken: string): Promise<AuthResponseDTO> => {
        const response = await api.post('/auth/refresh', { refreshToken });
        return response.data;
    },

    changePassword: async (data: ChangePasswordDTO): Promise<void> => {
        await api.post('/auth/change-password', data);
    },

    getCurrentUser: async (): Promise<UserDTO> => {
        const response = await api.get('/users/me');
        return response.data;
    }
};