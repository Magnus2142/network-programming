#!/bin/bash
cd src/main/resources || exit
docker build -t my-gcc-app . && docker run --rm my-gcc-app