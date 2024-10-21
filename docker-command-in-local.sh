# Local Docker Command

cd ./community-service
./gradlew clean build
docker build --no-cache -t dyw1014/adore-be-community-service:latest .
cd ..

cd ./user-service
./gradlew clean build
docker build --no-cache -t dyw1014/adore-be-user-service:latest .
cd ..

cd ./gateway-service
./gradlew clean build
docker build --no-cache -t dyw1014/adore-be-gateway-service:latest .
cd ..

cd ./discovery-service
./gradlew clean build
docker build --no-cache -t dyw1014/adore-be-discovery-service:latest .
cd ..

cd ./config-service
./gradlew clean build
docker build --no-cache -t dyw1014/adore-be-config-service:latest .
cd ..

cd ./vault
docker build --no-cache -t dyw1014/adore-be-vault-service:latest .

# Push docker image to docker hub (needed docker login)

docker push dyw1014/adore-be-community-service
docker push dyw1014/adore-be-user-service
docker push dyw1014/adore-be-gateway-service
docker push dyw1014/adore-be-discovery-service

# Pull docker-compose image
docker-compose -f .\docker-compose.yml pull

# Run docker-compose
docker-compose -f .\docker-compose.yml up -d

# Stop docker-compose
docker-compose -f .\docker-compose.yml stop

# build and run docker image
docker-compose up --build -d