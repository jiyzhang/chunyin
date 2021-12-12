FROM java:8
ADD target/chunyin-0.0.1.jar chunyin.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/chunyin.jar"]
