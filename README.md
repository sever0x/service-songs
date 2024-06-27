
# REST API Jacksong Application

This is a Spring Boot application for managing songs, artists, and related data. It provides a RESTful API for creating, retrieving, updating, and deleting songs and artists.

## Features

- Create, read, update, and delete songs
- Create, read, update, and delete artists
- Fetch a list of songs with pagination and filtering options
- Generate an Excel report of songs based on artist ID or album
- Import songs from a JSON file

## Technologies Used

- Java 17
- Spring Boot
- PostgreSQL
- Docker

## Running the Application

This application can be run using Docker Compose. Make sure you have Docker and Docker Compose installed on your machine.

1. Create a `.env` file in the root directory of the project with the following contents:

```
DB_URL=jdbc:postgresql://postgres:5432/jacksong
DB_PASSWORD=your_db_password
DB_USERNAME=your_db_username
```

Replace `your_db_password` and `your_db_username` with your actual PostgreSQL credentials.

2. Run the following command to start the application and the PostgreSQL database:

```
docker-compose up
```

The application will be available at `http://localhost:8080`.

## API Endpoints

### Artist Endpoints

- `POST /api/artist`: Create a new artist
    - Request Body:
      ```json
      {
        "name": "Artist Name",
        "country": "Country"
      }
      ```

- `GET /api/artist`: Get a list of all artists

- `PUT /api/artist/{id}`: Update an existing artist
    - Request Body:
      ```json
      {
        "name": "Updated Artist Name",
        "country": "Updated Country"
      }
      ```

- `DELETE /api/artist/{id}`: Delete an artist by ID

### Song Endpoints

- `POST /api/song`: Create a new song
    - Request Body:
      ```json
      {
        "title": "Song Title",
        "artistId": 1,
        "album": "Album Name",
        "genres": "Genre1,Genre2",
        "duration": 180,
        "releaseYear": 2022
      }
      ```

- `GET /api/song/{id}`: Get details of a song by ID

- `PUT /api/song/{id}`: Update an existing song
    - Request Body: Same as `POST /api/song`

- `DELETE /api/song/{id}`: Delete a song by ID

- `POST /api/song/_list`: Get a list of songs with pagination and filtering options
    - Request Body:
      ```json
      {
        "artistId": 1,
        "album": "Album Name",
        "page": 0,
        "size": 10
      }
      ```

- `POST /api/song/_report`: Generate an Excel report of songs based on artist ID or album
    - Request Body:
      ```json
      {
        "artistId": 1,
        "album": "Album Name"
      }
      ```

- `POST /api/song/upload`: Import songs from a JSON file
    - Request Body: `MultipartFile` with the JSON file
    - JSON Example (save as .json file):
       ```json
       [
        {
          "title": "Bohemian Rhapsody",
          "artist": {
            "name": "True Queen",
            "country": "UK"
          },
          "album": "A Night at the Opera",
          "genres": "Rock, Progressive Rock",
          "duration": 354,
          "releaseYear": 1975
        },
        {
          "title": "Imagine",
          "artist": {
            "name": "Jonny Lennon",
            "country": "UK"
          },
          "album": "Imagine",
          "genres": "Rock, Pop Rock",
          "duration": 184,
          "releaseYear": 1971
        },
        {
          "title": "Stairway to Heaven",
          "artist": {
            "name": "Led Zipkin",
            "country": "UK"
          },
          "album": "Led Zeppelin IV",
          "genres": "Rock, Folk Rock",
          "duration": 482,
          "releaseYear": 1971
        }
      ]
      ```