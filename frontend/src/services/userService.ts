import api from './api';
import { UserDTO, CreateUserDTO, UpdateUserDTO, UserStatus } from '../types/user';

export const userService = {
    getAll: async (): Promise<UserDTO[]> => {
        const response = await api.get('/users');
        return response.data;
    },

    getById: async (id: string): Promise<UserDTO> => {
        const response = await api.get(`/users/${id}`);
        return response.data;
    },

    getByEmail: async (email: string): Promise<UserDTO> => {
        const response = await api.get(`/users/email/${email}`);
        return response.data;
    },

    create: async (user: CreateUserDTO): Promise<UserDTO> => {
        const response = await api.post('/users', user);
        return response.data;
    },

    update: async (id: string, user: UpdateUserDTO): Promise<UserDTO> => {
        const response = await api.patch(`/users/${id}`, user);
        return response.data;
    },

    delete: async (id: string): Promise<void> => {
        await api.delete(`/users/${id}`);
    },

    changeStatus: async (id: string, status: UserStatus): Promise<void> => {
        await api.patch(`/users/${id}/status`, null, { params: { status } });
    }
};