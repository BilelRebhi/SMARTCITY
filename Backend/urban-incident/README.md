# Urban Incident Management Microservice

## Description
Smart City Urban Incident Management microservice for handling citizen reports, work orders, interventions, and notifications.

## Technology Stack
- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- Maven

## Entities
- **Incident**: Citizen-reported urban incidents
- **WorkOrder**: Tasks generated from incidents
- **Intervention**: Technician work performed on work orders
- **ResolutionConfirmation**: Citizen feedback on resolved incidents
- **Notification**: System notifications

## Running the Application

### Prerequisites
- Java 17 or higher
- PostgreSQL
- Maven

### Setup
1. Create PostgreSQL database using `database/init.sql`
2. Configure database connection in `application.properties`
3. Run the application:
```bash
mvn spring-boot:run
```

### Default Port
The service runs on port 8085

## API Endpoints

### Incidents
- `POST /api/incidents` - Create incident
- `GET /api/incidents` - Get all incidents
- `GET /api/incidents/{id}` - Get incident by ID
- `GET /api/incidents/citizen/{citizenId}` - Get incidents by citizen
- `GET /api/incidents/status/{status}` - Get incidents by status
- `PUT /api/incidents/{id}` - Update incident
- `PATCH /api/incidents/{id}/status` - Update incident status
- `DELETE /api/incidents/{id}` - Delete incident

### Work Orders
- `POST /api/work-orders` - Create work order
- `GET /api/work-orders` - Get all work orders
- `GET /api/work-orders/{id}` - Get work order by ID
- `GET /api/work-orders/incident/{incidentId}` - Get work orders by incident
- `GET /api/work-orders/technician/{technicianId}` - Get work orders by technician
- `GET /api/work-orders/status/{status}` - Get work orders by status
- `PUT /api/work-orders/{id}` - Update work order
- `PATCH /api/work-orders/{id}/assign` - Assign technician
- `PATCH /api/work-orders/{id}/status` - Update work order status
- `DELETE /api/work-orders/{id}` - Delete work order

### Interventions
- `POST /api/interventions` - Create intervention
- `GET /api/interventions` - Get all interventions
- `GET /api/interventions/{id}` - Get intervention by ID
- `GET /api/interventions/work-order/{workOrderId}` - Get intervention by work order
- `GET /api/interventions/technician/{technicianId}` - Get interventions by technician
- `PUT /api/interventions/{id}` - Update intervention
- `DELETE /api/interventions/{id}` - Delete intervention

### Resolution Confirmations
- `POST /api/confirmations` - Create confirmation
- `GET /api/confirmations` - Get all confirmations
- `GET /api/confirmations/{id}` - Get confirmation by ID
- `GET /api/confirmations/incident/{incidentId}` - Get confirmation by incident
- `DELETE /api/confirmations/{id}` - Delete confirmation

### Notifications
- `POST /api/notifications` - Create notification
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/{id}` - Get notification by ID
- `GET /api/notifications/recipient/{recipientId}` - Get notifications by recipient
- `GET /api/notifications/recipient/{recipientId}/unread` - Get unread notifications
- `PATCH /api/notifications/{id}/read` - Mark notification as read
- `DELETE /api/notifications/{id}` - Delete notification
