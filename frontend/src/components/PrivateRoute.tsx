import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { UserRole } from '../types/user';

interface PrivateRouteProps {
    children: React.ReactNode;
    requiredRole?: UserRole;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ children, requiredRole }) => {
    const { isAuthenticated, user, isLoading } = useAuth();

    if (isLoading) {
        return <div className="loading"></div>;
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (requiredRole && user?.role !== requiredRole && user?.role !== UserRole.ADMIN) {
        return <Navigate to="/" replace />;
    }

    return <>{children}</>;
};

export default PrivateRoute;