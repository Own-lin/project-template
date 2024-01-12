# build jenkins
docker run --name jenkins-docker -d --rm \
  -p 2378:8080 \
  -p 2377:50000 \
  -p 2376:2376 \
  --volume /env:/env \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  jenkins/jenkins:latest
