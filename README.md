# Rv-park-tenancy microservice

## Build the image

```bash
docker build -t rv-park-tenancy .
```

## Create network for all our microservices

```bash
docker network create rso
```

## Run database in network
```bash
docker run -d --name pg-rv-park-tenancy --network="rso" -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=rvs -p 5436:5432 postgres:13
```

## Run the container in network

```bash
docker run -p 8088:8088 --name rv-park-tenancy --network="rso" -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-rv-park-tenancy:5432/rv-park-tenancies rv-park-tenancy
```

## Run the container from Docker hub in network

```bash
docker run -p 8088:8088 --name rv-park-tenancy --network="rso" anzeha/rv-park-tenancy:latest
```
