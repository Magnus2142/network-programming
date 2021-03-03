FROM gcc:latest
COPY . /src/myapp
WORKDIR /src/myapp
RUN g++ -o myapp programToCompile.cpp
CMD ["./myapp"]
