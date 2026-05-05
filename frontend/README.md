# LifePath

Decision-based life simulator game with a React frontend and Spring Boot backend.

## Project Structure

- `backend/` - Spring Boot REST API + game engine + SQL seed
- `frontend/` - React + Vite + TypeScript + Tailwind UI

## Backend Run

1. Create database `lifepath` in PostgreSQL (or use H2 dev profile).
2. Update credentials in `backend/src/main/resources/application.yml` if needed.
3. Run:
   - `cd backend`
   - `./mvnw spring-boot:run` (or `mvn spring-boot:run`)
4. For H2 in-memory mode:
   - `mvn spring-boot:run -Dspring-boot.run.profiles=dev`

Backend starts at `http://localhost:8080`.

## Frontend Run

1. `cd frontend`
2. `npm install`
3. `npm run dev`

Frontend starts at `http://localhost:5173`.

## API Endpoints

- `POST /api/games/start`
- `GET /api/games/{gameId}`
- `POST /api/games/{gameId}/choose`
- `GET /api/games/{gameId}/history`
- `GET /api/games/{gameId}/ending`
