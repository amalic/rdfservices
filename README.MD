## About
This project aims to be a template for generating OpenAPI webservices for RDF-Endoints through [SpringDoc](https://github.com/springdoc/springdoc-openapi). It runs out of the box with Ontotext GraphDB and serves only generic webservices useful for exploring RDF Endpoints. It can be easily extended with custom webservices and secured thanks to [Spring Security](https://spring.io/guides/gs/securing-web/).

<p align="center"><img width="300" src="https://s3-us-west-2.amazonaws.com/assertible/integrations/OpenAPI-Logo-Pantone.png"></p>

## Checkout
```
git clone --recursive https://github.com/amalic/rdfservices.git
```
*Please note that GraphDb Free binaries have to be downloaded from the [Ontotext website](https://www.ontotext.com/products/graphdb/graphdb-free/) seperately. This example was tested with GraphDB 9.1.1 free edition.Download the zip archive into the graphdb-free folder.*
## Run

<p align="center"><img width="350" src="https://i1.wp.com/exabig.com/wp-content/uploads/2017/04/docker_compose.png"></p>

For volume mounts and other settings you can create a file called `docker-compose-local.yml`.

**Example**
```
version: "3"
services:
  rdfservices:
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
*This example expects a repository called **scigraph** to exist and be available without login. It will create a `./data` subdirectory for graph-db database data and a direcory for for server-side data imports. Don't forget to adjust your `docker-compose-local.yml` file accordingly in case you are using a newer version of GraphDb.*

**Start via docker-compose**
```
docker-compose \
  -f docker-compose.yml \
  -f docker-compose-local.yml \
  up -d --build --force-recreate
```
*First build can take some while. Maven dependencies will only be redownloaded when* `pom.xml` *changes.*

### Try it out: 
- Swagger-UI at http://localhost/swagger-ui.html
- OpenAPI Docs at http://localhost/v3/api-docs
- GraphDb at http://localhost:7200/
