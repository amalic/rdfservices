## About
This project aims to generate useful webservices for the the BioLink Api.
For now it serves only Swagger V2 services, since it depends on Springfox.
## Checkout
```
git clone --recursive https://github.com/amalic/rdfservices.git
```
*Please note that [GraphDb Free](https://www.ontotext.com/products/graphdb/graphdb-free/) binaries have to be downloaded from the OntoText website seperately*
## Run with docker-compose
```
docker compose up -d --build --force-recreate
```

### Try it out: 
- API at http://localhost/
- GraphDb at http://localhost:7200/
