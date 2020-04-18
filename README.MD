## About
This project aims to be a template for generating OpenAPI webservices through [SpringDoc](https://github.com/springdoc/springdoc-openapi) for RDF-Endoints. It runs out of the box with OntoText GraphDB and serves only generic webservices useful for exploring RDF Endpoints. It can be easily extended with custom webservices and secured thanks to [Spring Security](https://spring.io/guides/gs/securing-web/).
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
  webservice:
    environment:
      - ENDPOINT=http://graphdb:7200/repositories/scigraph
  graphdb:
    build:
      args:
        - edition=free
        - version=9.1.1
    volumes:
      - ./data/graphdb/:/opt/graphdb/home/
      - ./data/imports/:/root/graphdb-import/
```
*This example expects a repository called **scigraph** to exist and be available without login. It will create a `./data` subdirectory for graph-db database data and a direcory for for server-side data imports.*

**Start via docker-compose**
```
docker-compose \
  -f docker-compose.yml \
  -f docker-compose-local.yml \
  up -d --build --force-recreate
```
*First build can take some while. Maven dependencies will only be redownloaded when* `pom.xml` *changes.*

### Try it out: 
- API at http://localhost/
- OpenAPI Docs at http://localhost/v3/api-docs
- GraphDb at http://localhost:7200/
