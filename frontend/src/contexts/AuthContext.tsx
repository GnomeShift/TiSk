import React, { createContext, useContext, useState, useEffect, ReactNode, useCallback, useMemo } from 'react';
import axios from 'axios';
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

// Check if error is auth-related
const isAuthError = (error: unknown): boolean => {
    if (axios.isAxiosError(error) && error.response?.status) {
        const status = error.response.status;
        return status === 401 || status === 403;
    }
    return false;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [state, setState] = useState<AuthState>({
        user: null,
        accessToken: null,
        refreshToken: null,
        isAuthenticated: false,
        isLoading: true
    });

    // Memoized logout
    const logout = useCallback(() => {
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
    }, []);

    // Listening for logout event from interceptor
    useEffect(() => {
        const handleLogoutEvent = () => {
            logout();
        };

        window.addEventListener('auth:logout', handleLogoutEvent);
        return () => window.removeEventListener('auth:logout', handleLogoutEvent);
    }, [logout]);

    // Check auth on mount
    useEffect(() => {
        let isMounted = true;

        const checkAuth = async () => {
            const accessToken = localStorage.getItem('access_token');
            const refreshToken = localStorage.getItem('refresh_token');

            if (!accessToken || !refreshToken) {
                if (isMounted) setState(prev => ({ ...prev, isLoading: false }));
                return;
            }

            try {
                const currentUser = await authService.getCurrentUser();

                if (isMounted) {
                    setState({
                        user: currentUser,
                        accessToken,
                        refreshToken,
                        isAuthenticated: true,
                        isLoading: false
                    });
                }
            } catch (error) {
                if (!isMounted) return;
                if (isAuthError(error)) {
                    logout();
                    return;
                }

                const cachedUser = localStorage.getItem('user');
                if (cachedUser) {
                    try {
                        setState({
                            user: JSON.parse(cachedUser),
                            accessToken,
                            refreshToken,
                            isAuthenticated: true,
                            isLoading: false
                        });
                        return;
                    } catch(err) {
                        console.error(err)
                    }
                }
                setState(prev => ({ ...prev, isLoading: false }));
            }
        };

        void checkAuth();

        return () => {
            isMounted = false;
        };
    }, [logout]);

    const login = useCallback(async (credentials: LoginDTO) => {
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
    }, []);

    const register = useCallback(async (data: RegisterDTO) => {
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
    }, []);

    const updateUser = useCallback((user: UserDTO) => {
        localStorage.setItem('user', JSON.stringify(user));
        setState(prev => ({ ...prev, user }));
    }, []);

    const changePassword = useCallback(async (data: ChangePasswordDTO) => {
        await authService.changePassword(data);
    }, []);

    const contextValue = useMemo<AuthContextType>(() => ({
        ...state,
        login,
        register,
        logout,
        updateUser,
        changePassword
    }), [state, login, register, logout, updateUser, changePassword]);

    return (
        <AuthContext.Provider value={contextValue}>
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