.PHONY: build run
SOURCES=./src/com/company/Board/Bitboard.java \
		./src/com/company/Board/BoardCommands.java \
		./src/com/company/Board/BoardState.java \
		./src/com/company/Board/MoveHistory.java \
		./src/com/company/Board/PSqTable.java \
		./src/com/company/Database.java \
		./src/com/company/Main.java \
		./src/com/company/Printer.java \
		./src/com/company/XBoardProtocol.java \
		./src/com/company/MoveAndSearch/Eval.java \
		./src/com/company/MoveAndSearch/Move.java \
		./src/com/company/MoveAndSearch/MoveGenerator.java \
		./src/com/company/MoveAndSearch/Negamax.java \
		./src/com/company/MoveAndSearch/Perft.java \
		./src/com/company/MoveAndSearch/SlidingPieceGenerator.java

NAME="Garbigi.jar"
JAVAFLAGS=-Xmx1024m -Xss1024m

build:
	javac -d build $(SOURCES)
	mkdir META-INF 2> /dev/null
	touch META-INF/MANIFEST.MF 2> /dev/null
	echo "Main-Class: com.company.Main" > META-INF/MANIFEST.MF
	jar cmvf META-INF/MANIFEST.MF $(NAME) -C build .

run:
	java ${JAVAFLAGS} -jar $(NAME)

clean:
	rm -rf META-INF
	rm -f Garbigi.jar
	rm -rf build