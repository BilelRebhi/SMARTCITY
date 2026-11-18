export interface Incident {
  id?: number;
  title: string;
  description: string;
  category: IncidentCategory;
  photoUrls: string[];
  latitude: number;
  longitude: number;
  createdAt?: Date;
  status: IncidentStatus;
  priority?: Priority;
  severity?: Severity;
  citizenId: number;
}

export enum IncidentCategory {
  ROAD_DAMAGE = 'ROAD_DAMAGE',
  STREET_LIGHT = 'STREET_LIGHT',
  WATER_LEAK = 'WATER_LEAK',
  WASTE_MANAGEMENT = 'WASTE_MANAGEMENT',
  PUBLIC_SAFETY = 'PUBLIC_SAFETY',
  VANDALISM = 'VANDALISM',
  NOISE_POLLUTION = 'NOISE_POLLUTION',
  TRAFFIC = 'TRAFFIC',
  PARK_MAINTENANCE = 'PARK_MAINTENANCE',
  OTHER = 'OTHER'
}

export enum IncidentStatus {
  REPORTED = 'REPORTED',
  IN_PROGRESS = 'IN_PROGRESS',
  RESOLVED = 'RESOLVED',
  CLOSED = 'CLOSED',
  CANCELLED = 'CANCELLED'
}

export enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export enum Severity {
  MINOR = 'MINOR',
  MODERATE = 'MODERATE',
  MAJOR = 'MAJOR',
  SEVERE = 'SEVERE'
}
