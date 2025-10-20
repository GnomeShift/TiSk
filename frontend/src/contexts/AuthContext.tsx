import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthState, LoginDTO, RegisterDTO, ChangePasswordDTO } from '../types/auth';
import { UserDTO } from '../types/user'
import { authService } from '../services/authService';

interface AuthContextType extends AuthState {
    login: (credentials: LoginDTO) => Promise<void>;
    register: (data: RegisterDTO) => Promise<void>;
    logout: () => void;
    updateUser: (user: UserDTO) => void;
    changePassword: (data: ChangePasswordDTO) => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [state, setState] = useState<AuthState>({
        user: null,
        accessToken: null,
        refreshToken: null,
        isAuthenticated: false,
        isLoading: true
    });

    useEffect(() => {
        checkAuth();
    }, []);

    const checkAuth = async () => {
        const accessToken = localStorage.getItem('access_token');
        const refreshToken = localStorage.getItem('refresh_token');

        if (accessToken && refreshToken) {
            try {
                const currentUser = await authService.getCurrentUser();
                setState({
                    user: currentUser,
                    accessToken,
                    refreshToken,
                    isAuthenticated: true,
                    isLoading: false
                });
            } catch (error) {
                console.error('Auth check failed:', error);
                logout();
            }
        } else {
            setState(prev => ({ ...prev, isLoading: false }));
        }
    };

    const login = async (credentials: LoginDTO) => {
        try {
            const response = await authService.login(credentials);

            localStorage.setItem('access_token', response.accessToken);
            localStorage.setItem('refresh_token', response.refreshToken);
            localStorage.setItem('user', JSON.stringify(response.user));

            setState({
                user: response.user,
                accessToken: response.accessToken,
                refreshToken: response.refreshToken,
                isAuthenticated: true,
                isLoading: false
            });
        } catch (error) {
            console.error('Login failed:', error);
            throw error;
        }
    };

    const register = async (data: RegisterDTO) => {
        try {
            const response = await authService.register(data);

            localStorage.setItem('access_token', response.accessToken);
            localStorage.setItem('refresh_token', response.refreshToken);
            localStorage.setItem('user', JSON.stringify(response.user));

            setState({
                user: response.user,
                accessToken: response.accessToken,
                refreshToken: response.refreshToken,
                isAuthenticated: true,
                isLoading: false
            });
        } catch (error) {
            console.error('Registration failed:', error);
            throw error;
        }
    };

    const logout = () => {
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        localStorage.removeItem('user');

        setState({
            user: null,
            accessToken: null,
            refreshToken: null,
            isAuthenticated: false,
            isLoading: false
        });
    };

    const updateUser = (user: UserDTO) => {
        localStorage.setItem('user', JSON.stringify(user));
        setState(prev => ({ ...prev, user }));
    };

    const changePassword = async (data: ChangePasswordDTO) => {
        await authService.changePassword(data);
    };

    return (
        <AuthContext.Provider value={{ ...state, login, register, logout, updateUser, changePassword }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};