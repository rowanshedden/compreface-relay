# CompreFace DTL Relay
A simple service that connects to the Gallery Service via websocket and enrols the traveller into CompreFace.

## CompreFace API Usage
Accept the ADD traveller message from the Gallery Service over the websocket interface and call the /api/v1/recognition/faces?subject={{subject_name}} API on the CompreFace service.

e.g. API call POST - http://192.168.1.181/api/v1/recognition/faces?subject=

# Installation
First time only, download the source project from GitHub.
```
git clone https://github.com/rowanshedden/compreface-relay-service.git
```

# GitHub Repository Updates
Update the source code.

Add the updates to the local repo:
```
git add .
```
Commit the updates into the local repo:
```
git commit -m"a brief description of the update"
```
Send the updates to the remote GitHub repo:
```
git push
```

To retrieve updates from the remote GitHub repo:
```
git pull
```

# Build and Run
Perform a clean install
```
mvn clean install
```

Run the application with default values
```
mvn spring-boot:run
```

Run the application with a specific profile
```
mvn spring-boot:run -Dspring-boot.run.profiles=damien
```

# Local Browser Access
Access the webapp from a browser via http://localhost:8005


