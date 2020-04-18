## About
This project aims to generate useful webservices for the the BioLink Api.
For now it serves only Swagger V2 services, since it depends on Springfox.
## Checkout
```
git clone --recursive https://github.com/amalic/rdfservices.git
```
*Please note that GraphDb Free binaries have to be downloaded from the [OntoText website](https://www.ontotext.com/products/graphdb/graphdb-free/) seperately*
## Run with docker-compose
```
docker compose up -d --build --force-recreate
```

### Try it out: 
- API at http://localhost/
- API Docs at http://localhost/v3/api-docs
- GraphDb at http://localhost:7200/
