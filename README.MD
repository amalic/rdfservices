## About
This project aims to generate useful webservices for the the BioLink Api.
For now it serves only Swagger V2 services, since it depends on Springfox.
## Checkout
```
git clone --recursive https://github.com/amalic/rdfservices.git
```
*Please note that GraphDb Free binaries have to be downloaded from the [OntoText website](https://www.ontotext.com/products/graphdb/graphdb-free/) seperately. This example was tested with GraphDB 9.1.1 free edition.*
## Run
For volume mounts create a file called `docker-compose-local.yml`.

**Example**
```
version: "3"
services:
  graphdb:
    build:
      args:
        - edition=free
        - version=9.1.1
    volumes:
      - ./data/graphdb:/opt/graphdb/home
      - ./data/scigraph/:/root/graphdb-import/
```
**Start via docker-compose**
```
docker-compose \
  -f docker-compose.yml \
  -f docker-compose-local.yml \
  up -d --build --force-recreate
```
*The default configuration expects a repository called **scigraph** to exist and be available without login.*

### Try it out: 
- API at http://localhost/
- OpenAPI Docs at http://localhost/v3/api-docs
- GraphDb at http://localhost:7200/
