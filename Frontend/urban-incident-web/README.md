# Urban Incident Management - Frontend

This is the Angular frontend for the Urban Incident Management system.

## Features

### Modules
- **Incidents**: Report and track urban incidents
- **Work Orders**: Manage work orders generated from incidents
- **Interventions**: Track technician interventions
- **Notifications**: Real-time notifications system
- **Dashboard**: Overview of all incidents and activities

### Structure
```
src/
├── app/
│   ├── core/
│   │   ├── services/         # API services
│   │   ├── guards/           # Route guards
│   │   └── interceptors/     # HTTP interceptors
│   ├── shared/
│   │   ├── models/           # TypeScript interfaces
│   │   ├── components/       # Reusable components
│   │   ├── directives/       # Custom directives
│   │   └── pipes/            # Custom pipes
│   └── features/
│       ├── incidents/        # Incident management
│       ├── work-orders/      # Work order management
│       ├── interventions/    # Intervention tracking
│       ├── notifications/    # Notification center
│       └── dashboard/        # Main dashboard
├── assets/
│   ├── images/
│   └── styles/
└── environments/
```

## Development

### Prerequisites
- Node.js (v18 or higher)
- npm

### Installation
```bash
npm install
```

### Development Server
```bash
npm start
```
Navigate to `http://localhost:4200/`

### Build
```bash
npm run build
```

### Running Tests
```bash
npm test
```

## API Configuration

The API endpoint is configured in `src/environments/environment.ts`:
- Development: `http://localhost:8085/api`
- Production: `/api`

## Services

- **IncidentService**: Manage incidents
- **WorkOrderService**: Manage work orders
- **InterventionService**: Manage interventions
- **NotificationService**: Handle notifications
