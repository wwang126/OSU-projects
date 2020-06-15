#include <iostream>
#include <stdio.h>

#include "ast.hpp"
#include "parser.hpp"


extern int yylex();

extern ASTBlock* programBlock;

int main(int argc, char* argv[]) {
  std::cout << "\tReading args!" << std::endl;
  std::cout << argv[0] << std::endl;
  if (!yylex()) {
    if (argc >= 1){
          std::cout << "\t args passed" << std::endl;
    }
    if(argc == 1 && argv[1].compare("-O") == 0){
      std::cout << "Optimizing!" << std::endl;
    }
    if (programBlock) {
      std::cout << generateGVSpec(programBlock);
    }
  }
  return 0;
}
