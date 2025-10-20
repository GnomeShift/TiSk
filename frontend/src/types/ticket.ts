import { UserDTO } from './user';

export interface TicketDTO {
    id: string;
    title: string;
    description: string;
    status: TicketStatus;
    priority: TicketPriority;
    reporter?: UserDTO;
    assignee?: UserDTO;
    createdAt: string;
    updatedAt: string;
}

export enum TicketStatus {
    OPEN = 'OPEN',
    IN_PROGRESS = 'IN_PROGRESS',
    CLOSED = 'CLOSED'
}

export enum TicketPriority {
    LOW = 'LOW',
    MEDIUM = 'MEDIUM',
    HIGH = 'HIGH',
    VERY_HIGH = 'VERY_HIGH'
}

export interface CreateTicketDTO {
    title: string;
    description: string;
    priority: TicketPriority;
    reporterId: string;
}

export interface UpdateTicketDTO {
    title: string;
    description: string;
    status: TicketStatus;
    priority: TicketPriority;
    reporterId?: string;
}