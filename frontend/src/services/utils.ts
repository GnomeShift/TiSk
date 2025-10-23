import { UserRole } from '../types/user';

export const getStatusColor = (status: string) => {
    switch (status) {
        case 'OPEN': return 'status-open';
        case 'IN_PROGRESS': return 'status-progress';
        case 'CLOSED': return 'status-closed';
        default: return '';
    }
};

export const getPriorityColor = (priority: string) => {
    switch (priority) {
        case 'VERY_HIGH': return 'priority-very-high';
        case 'HIGH': return 'priority-high';
        case 'MEDIUM': return 'priority-medium';
        case 'LOW': return 'priority-low';
        default: return '';
    }
};

export const getRoleLabel = (role: UserRole): string => {
    switch (role) {
        case UserRole.ADMIN: return 'Администратор';
        case UserRole.SUPPORT: return 'Поддержка';
        case UserRole.USER: return 'Пользователь';
        default: return role;
    }
};