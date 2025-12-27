./gradlew build -x test
cd docker
docker-compose --env-file .env.local build
docker-compose --env-file .env.local up -d