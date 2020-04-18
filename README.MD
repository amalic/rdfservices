## About
This project aims to generate useful webservices for the the BioLink Api.
For now it serves only Swagger V2 services, since it depends on Springfox.
## Build
First build and builds after modifying pom.xml might take some time, since the Dockerfile will resolve all dependencies from scratch. Once cached, builds should be significantly faster.
```
docker build --rm -f "Dockerfile" -t rdfservices:latest .
```
## Checkout
```
git clone --recursive https://github.com/amalic/ServiceFromData.git
```

## Run
```
docker compose up -d --build --force-recreate
```

### Try it out: 
- API at http://localhost/
- GraphDb at http://localhost:7200/
