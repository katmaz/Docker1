FROM java:8
RUN curl -L -o ./mysql-connector-java-5.1.6.jar https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.6/mysql-connector-java-5.1.6.jar
COPY ./Baza.java /usr/src/
COPY ./mysql-connector-java-5.1.6.jar /usr/src/
WORKDIR /usr/src/
RUN javac Baza.java
CMD ["java", "-classpath", "mysql-connector-java-5.1.6.jar:.","Baza"]
