FROM java
ENV version=0.2.0
MAINTAINER Tobias Eljasik-Swoboda ${version}
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ADD ./target/teek-${version}-SNAPSHOT.jar /opt/teek/target/teek-${version}-SNAPSHOT.jar
ADD ./teek.yml /opt/teek/target/teek.yml
RUN mkdir /opt/wordembeddings
ADD ./target/wordembeddings/skip-gram-wiki1stbill.txt /opt/wordembeddings/skip-gram-wiki1stbill.txt
RUN java -jar /opt/teek/target/teek-${version}-SNAPSHOT.jar server /opt/teek/target/teek.yml