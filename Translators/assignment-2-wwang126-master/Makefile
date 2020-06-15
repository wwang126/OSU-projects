all: parser

scan: main.cpp scanner.cpp
	g++ main.cpp scanner.cpp -o scan

scanner.cpp: scanner.l
	flex -o scanner.cpp scanner.l

parser.cpp parser.hpp: parser.y
	bison -d -o parser.cpp parser.y

parser: main.cpp scanner.cpp parser.cpp parser.hpp
	g++ main.cpp scanner.cpp parser.cpp -o parser

clean:
	rm -f scan scanner.cpp parser.cpp parser.hpp parser
