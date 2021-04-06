FROM adoptopenjdk/openjdk14:alpine-jre
ADD target/insuranceapi-0.0.1-SNAPSHOT.jar app.jar
ADD target/classes/input/vehicles.csv vehicles.csv
ADD target/classes/input/data.json data.json
ADD target/classes/output/TestWriter.csv testwriter.csv

ENTRYPOINT ["java","-jar","app.jar","vehicles.csv","data.json","testwriter.csv"]
