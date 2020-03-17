.PHONY: build run
SOURCES=./src/com/company/Board/Bitboard.java \
		./src/com/company/Board/BoardCommands.java \
		./src/com/company/Board/BoardState.java \
		./src/com/company/Board/MoveHistory.java \
		./src/com/company/Database.java \
		./src/com/company/Main.java \
		./src/com/company/Printer.java \
		./src/com/company/XBoardProtocol.java
NAME="Chessnut.jar"

build:
	javac -d build $(SOURCES)
	mkdir META-INF 2> /dev/null
	touch META-INF/MANIFEST.MF 2> /dev/null
	echo "Main-Class: com.company.Main" > META-INF/MANIFEST.MF
	jar cmvf META-INF/MANIFEST.MF $(NAME) -C build .

run:
	java -jar $(NAME)

clean:
	rm -rf META-INF
	rm -f Chessnut.jar
	rm -rf build