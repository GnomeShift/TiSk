import { CreateTicketDTO, TicketDTO, UpdateTicketDTO } from '../types/ticket.ts';
import api from './api.ts';
import {PaginatedResponse, PaginationParams} from '../types/pagination.ts';

export const ticketService = {
    getAll: async (params?: PaginationParams): Promise<PaginatedResponse<TicketDTO>> => {
        const response = await api.get('/tickets', { params });

        if (Array.isArray(response.data)) {
            const data = response.data as TicketDTO[];
            const page = params?.page || 1;
            const limit = params?.limit || 10;
            const start = (page - 1) * limit;
            const end = start + limit;

            let filteredData = [...data];

            if (params?.search) {
                const searchLower = params.search.toLowerCase();
                filteredData = filteredData.filter(ticket =>
                    ticket.title.toLowerCase().includes(searchLower) ||
                    ticket.description.toLowerCase().includes(searchLower) ||
                    ticket.id.toLowerCase().includes(searchLower)
                );
            }

            if (params?.status && params.status !== 'ALL') {
                filteredData = filteredData.filter(ticket => ticket.status === params.status);
            }

            if (params?.priority && params.priority !== 'ALL') {
                filteredData = filteredData.filter(ticket => ticket.priority === params.priority);
            }

            if (params?.sortBy) {
                filteredData.sort((a, b) => {
                    let aVal: any = a[params.sortBy as keyof TicketDTO];
                    let bVal: any = b[params.sortBy as keyof TicketDTO];

                    if (params.sortBy === 'priority') {
                        const priorityOrder = { 'VERY_HIGH': 4, 'HIGH': 3, 'MEDIUM': 2, 'LOW': 1 };
                        aVal = priorityOrder[aVal as keyof typeof priorityOrder];
                        bVal = priorityOrder[bVal as keyof typeof priorityOrder];
                    }

                    if (params.sortOrder === 'desc') {
                        return bVal > aVal ? 1 : -1;
                    }
                    return aVal > bVal ? 1 : -1;
                });
            }

            const paginatedData = filteredData.slice(start, end);

            return {
                data: paginatedData,
                total: filteredData.length,
                page,
                totalPages: Math.ceil(filteredData.length / limit),
                limit
            };
        }

        return response.data;
    },

    getMyTickets: async (): Promise<TicketDTO[]> => {
        const response = await api.get('/tickets/my');
        return response.data;
    },

    getById: async (id: string): Promise<TicketDTO> => {
        const response = await api.get(`/tickets/${id}`);
        return response.data;
    },

    create: async (ticket: CreateTicketDTO): Promise<TicketDTO> => {
        const response = await api.post('/tickets', ticket);
        return response.data;
    },

    update: async (id: string, ticket: UpdateTicketDTO): Promise<TicketDTO> => {
        const response = await api.patch(`/tickets/${id}`, ticket);
        return response.data;
    },

    assignTicket: async (id: string, assigneeId: string): Promise<TicketDTO> => {
        const response = await api.patch(`/tickets/${id}/assign`, null, {
            params: { assigneeId }
        });
        return response.data;
    },

    delete: async (id: string): Promise<void> => {
        await api.delete(`/tickets/${id}`);
    },
};