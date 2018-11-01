## Build
```
docker build --rm -f "Dockerfile" -t servicefromdata:latest .
```

## Run
```
docker run -it --rm -p 80:8080 servicefromdata
```

Try it out: http://localhost/d2s/hello?world