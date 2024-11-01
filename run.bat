@echo off
if exist out (
    rmdir /s /q out
)

mkdir out
cd src
javac -d ../out *.java
cd ..

java -cp out Main

pause