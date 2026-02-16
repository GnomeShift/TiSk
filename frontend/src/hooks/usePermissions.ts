import { useAuth } from '../contexts/AuthContext';
import { UserRole } from '../types/user';
import { TicketDTO } from '../types/ticket';
import { useMemo } from 'react';

export function usePermissions() {
    const { user } = useAuth();

    const userId = user?.id;
    const userRole = user?.role;

    return useMemo(() => {
        const isAdmin = userRole === UserRole.ADMIN;
        const isSupport = userRole === UserRole.SUPPORT;
        const isStaff = isAdmin || isSupport;

        return {
            userId,
            userRole,
            isAdmin,
            isSupport,
            isStaff,
            // General perms
            canManageUsers: isAdmin,
            canViewStatistics: isAdmin,
            canViewAllTickets: isStaff,
            canAssignTickets: isAdmin,
            canChangeTicketStatus: isStaff,
            canDeleteTickets: isAdmin,

            canEditTicket: (ticket: TicketDTO | null) => {
                if (!ticket || !userId) return false;
                return isStaff || ticket.reporter?.id === userId;
            },

            canTakeTicket: (ticket: TicketDTO | null) => {
                if (!ticket || !userId) return false;
                return isStaff && !ticket.assignee && ticket.status !== 'CLOSED';
            },

            canDeleteTicket: (ticket: TicketDTO | null) => {
                return isAdmin && !!ticket;
            }
        };
    }, [userId, userRole]);
}