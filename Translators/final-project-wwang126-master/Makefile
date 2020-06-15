all: parse

parser.cpp parser.hpp: parser.y
	bison -d -o parser.cpp parser.y

scanner.cpp: scanner.l
	flex -o scanner.cpp scanner.l

parse: main.cpp ast.cpp parser.cpp scanner.cpp
	g++ -std=c++11 main.cpp ast.cpp parser.cpp scanner.cpp `llvm-config-7.0-64 --cppflags --ldflags --libs --system-libs all` -o parse

test: output.o test.cpp 
	g++ output.o test.cpp -o testprog

clean:
	rm -f parse scanner.cpp parser.cpp parser.hpp testprog output.o
