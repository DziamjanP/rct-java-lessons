javac -d bin src/*.java
cd bin
jar cvfe ../app.jar src.MainFrame src/*.class
chmod a+x ../app.jar