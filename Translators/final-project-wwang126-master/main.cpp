#include <iostream>
#include <map>


//LLVM include statements
#include "llvm/IR/LLVMContext.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/Module.h"
#include "llvm/IR/Verifier.h"
#include "llvm/IR/Value.h"

#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Support/TargetSelect.h"
#include "llvm/Support/TargetRegistry.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/Support/FileSystem.h"
#include "llvm/Target/TargetOptions.h"
#include "llvm/Target/TargetMachine.h"

#include "llvm/Transforms/Scalar.h"
#include "llvm/Transforms/Scalar/GVN.h"
#include "llvm/Transforms/Utils.h"


#include "ast.hpp"
#include "parser.hpp"


static std::map<std::string, llvm::Value*> TheSymbolTable;

extern int yylex();

extern ASTBlock* programBlock;



int main(int argc, char* argv[]) {

  if (!yylex()) {
    if (programBlock == NULL){
      std::cout << "Program block is null" << std::endl;
    }
    if (programBlock) {
      if(argc > 1){
        std::string arg1 = argv[1];
        if(arg1 == "-O"){
          std::cout << "Optimizing!" << std::endl;
          generateIRCodeOp(programBlock);
        }
      } else{
        generateIRCode(programBlock);
      }
    }
  }
  return 0;
}
