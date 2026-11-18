export interface Notification {
  id?: number;
  recipientId: number;
  recipientType: RecipientType;
  message: string;
  notificationType: NotificationType;
  isRead: boolean;
  createdAt?: Date;
}

export enum RecipientType {
  CITIZEN = 'CITIZEN',
  TECHNICIAN = 'TECHNICIAN',
  ADMIN = 'ADMIN',
  SYSTEM = 'SYSTEM'
}

export enum NotificationType {
  EMAIL = 'EMAIL',
  SMS = 'SMS',
  PUSH_NOTIFICATION = 'PUSH_NOTIFICATION',
  IN_APP = 'IN_APP'
}
