## Build
```
docker build --rm -f "Dockerfile" -t servicefromdata:latest .
```

## Run
```
docker run -it --rm -p 80:8080 servicefromdata
```

### Try it out: 
http://localhost/biolink/drugbank/Drug

### Swagger-Apidoc:
http://localhost/v2/api-docs

### Swagger UI
http://localhost/swagger-ui.html