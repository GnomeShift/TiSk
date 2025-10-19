export interface UserDTO {
    id: string;
    email: string;
    login: string;
    firstName: string;
    lastName: string;
    phoneNumber?: string;
    department?: string;
    position?: string;
    role: UserRole;
    status: UserStatus;
    createdAt: string;
    updatedAt: string;
    lastLoginAt?: string;
}

export enum UserRole {
    ADMIN = 'ADMIN',
    SUPPORT = 'SUPPORT',
    USER = 'USER'
}

export enum UserStatus {
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    SUSPENDED = 'SUSPENDED'
}

export interface CreateUserDTO {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    login: string;
    phoneNumber?: string;
    department?: string;
    position?: string;
    role: UserRole;
}

export interface UpdateUserDTO {
    email?: string;
    firstName?: string;
    lastName?: string;
    login?: string;
    phoneNumber?: string;
    department?: string;
    position?: string;
    role?: UserRole;
    status?: UserStatus;
}