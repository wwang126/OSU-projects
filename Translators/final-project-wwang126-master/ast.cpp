#include <sstream>

#include "ast.hpp"
#include "parser.hpp"




//Globals for use in generation
static llvm::LLVMContext TheContext;
static llvm::IRBuilder<> TheBuilder(TheContext);
static llvm::Module* TheModule;

static std::map<std::string, llvm::Value*> TheSymbolTable;


/*
 * Simple template function to convert a value of any type to a string
 * representation.  The type must have an insertion operator (i.e. operator<<).
 */
template <class T>
std::string toString(const T& val) {
  std::ostringstream out;
  out << val;
  return out.str();
}

//Helper function from example code
llvm::Value* numericConstant(float val) {
  return llvm::ConstantFP::get(TheContext, llvm::APFloat(val));
  //return NULL;
}

//Helper function from example code
void generateObjCode(const std::string& filename) {
  std::string targetTriple = llvm::sys::getDefaultTargetTriple();

  llvm::InitializeAllTargetInfos();
  llvm::InitializeAllTargets();
  llvm::InitializeAllTargetMCs();
  llvm::InitializeAllAsmParsers();
  llvm::InitializeAllAsmPrinters();

  std::string error;
  const llvm::Target* target =
    llvm::TargetRegistry::lookupTarget(targetTriple, error);
  if (!target) {
    std::cerr << error << std::endl;
  } else {
    std::string cpu = "generic";
    std::string features = "";
    llvm::TargetOptions options;
    llvm::TargetMachine* targetMachine =
      target->createTargetMachine(targetTriple, cpu, features,
        options, llvm::Optional<llvm::Reloc::Model>());

    TheModule->setDataLayout(targetMachine->createDataLayout());
    TheModule->setTargetTriple(targetTriple);

    std::error_code ec;
    llvm::raw_fd_ostream file(filename, ec, llvm::sys::fs::F_None);
    if (ec) {
      std::cerr << "Could not open output file: " << ec.message() << std::endl;
    } else {
      llvm::TargetMachine::CodeGenFileType type = llvm::TargetMachine::CGFT_ObjectFile;
      llvm::legacy::PassManager pm;
      if (targetMachine->addPassesToEmitFile(pm, file, NULL, type)) {
        std::cerr << "Unable to emit target code" << std::endl;
      } else {
        pm.run(*TheModule);
        file.close();
      }
    }
  }
}

//Helper fucntion from example code
void doOptimizations(llvm::Function* fn) {
  llvm::legacy::FunctionPassManager* fpm =
    new llvm::legacy::FunctionPassManager(TheModule);

  fpm->add(llvm::createPromoteMemoryToRegisterPass());
  // fpm->add(llvm::createInstructionCombiningPass());
  fpm->add(llvm::createReassociatePass());
  fpm->add(llvm::createGVNPass());
  fpm->add(llvm::createCFGSimplificationPass());

  fpm->run(*fn);
}


// Main function to generate IR Code
llvm::Value* generateIRCode(ASTNode* node){
  if(node == NULL){
    std::cout << "Node Null IRgen" << std::endl;
  }
  TheModule = new llvm::Module("Python compiler", TheContext);

    llvm::FunctionType* fooFnType = llvm::FunctionType::get(
      llvm::Type::getFloatTy(TheContext), false
    );

    llvm::Function* fooFn = llvm::Function::Create(
      fooFnType,
      llvm::GlobalValue::ExternalLinkage,
      "py1",
      TheModule
    );

  llvm::BasicBlock* entryBlock =
    llvm::BasicBlock::Create(TheContext, "entry", fooFn);
  TheBuilder.SetInsertPoint(entryBlock);



  llvm::Value* val = node->generateIR();
  //Add void return at end.
  TheBuilder.CreateRet(TheBuilder.CreateLoad(TheSymbolTable["sphere_surf_area"], "sphere_surf_area"));
  TheModule->print(llvm::outs(), NULL);

  std::string outputfile = "output.o";
  generateObjCode(outputfile);
  return val;
}

//Optimized version to generate helper code
llvm::Value* generateIRCodeOp(ASTNode* node){;
  if(node == NULL){
    std::cout << "Node Null IRgen" << std::endl;
  }
  TheModule = new llvm::Module("Python compiler", TheContext);

    llvm::FunctionType* fooFnType = llvm::FunctionType::get(
      llvm::Type::getFloatTy(TheContext), false
    );

    llvm::Function* py1 = llvm::Function::Create(
      fooFnType,
      llvm::GlobalValue::ExternalLinkage,
      "py1",
      TheModule
    );

  llvm::BasicBlock* entryBlock =
    llvm::BasicBlock::Create(TheContext, "entry", py1);
  TheBuilder.SetInsertPoint(entryBlock);

  llvm::Value* val = node->generateIR();
  //Add void return at end.
  TheBuilder.CreateRet(TheBuilder.CreateLoad(TheSymbolTable["sphere_surf_area"], "sphere_surf_area"));
  //Optmize the code Here
  doOptimizations(py1);
  TheModule->print(llvm::outs(), NULL);

  std::string outputfile = "output.o";
  generateObjCode(outputfile);
  return NULL;
}
//Adds Identifiers
llvm::Value* ASTIdentifier::generateIR() const {
  llvm::Value* val = TheSymbolTable[this->name->c_str()];
  if (!val){
    std::cerr << "\tInvalid variable name: " << *this->name << std::endl;
    return NULL;
  }
  return TheBuilder.CreateLoad(val, this->name->c_str());
}

//Adds floats
llvm::Value* ASTFloat::generateIR() const {
  return numericConstant(this->value);
}

//Adds integers
llvm::Value* ASTInteger::generateIR() const {
  return numericConstant(this->value);
}

//TODO: Get this working
llvm::Value* ASTBoolean::generateIR() const {
  return NULL;
}

//Handles binary expressions
llvm::Value* ASTBinaryOperatorExpression::generateIR() const {
  llvm::Value* lhs_val = this->lhs->generateIR();
  llvm::Value* rhs_val = this->rhs->generateIR();
  if (!lhs_val || !rhs_val){
    std::cout << "\tbinary op lhs rhs null" << std::endl;
    return NULL;
  }
  switch (this->op) {
    case PLUS:
      return TheBuilder.CreateFAdd(lhs_val, rhs_val, "addtmp");
    case MINUS:
      return TheBuilder.CreateFSub(lhs_val, rhs_val, "subtmp");
    case TIMES:
      return TheBuilder.CreateFMul(lhs_val, rhs_val, "multmp");
    case DIVIDEDBY:
      return TheBuilder.CreateFDiv(lhs_val, rhs_val, "divtmp");
    case EQ:
      lhs_val = TheBuilder.CreateFCmpUEQ(lhs_val, rhs_val, "eqtmp");
      return TheBuilder.CreateUIToFP(lhs_val, llvm::Type::getFloatTy(TheContext), "booltmp");
    case NEQ:
      lhs_val = TheBuilder.CreateFCmpUNE(lhs_val, rhs_val, "neqtmp");
      return TheBuilder.CreateUIToFP(lhs_val, llvm::Type::getFloatTy(TheContext), "booltmp");
    case GT:
      lhs_val = TheBuilder.CreateFCmpUGT(lhs_val, rhs_val, "gttmp");
      return TheBuilder.CreateUIToFP(lhs_val, llvm::Type::getFloatTy(TheContext), "booltmp");
    case GTE:
      lhs_val = TheBuilder.CreateFCmpUGE(lhs_val, rhs_val, "gtetmp");
      return TheBuilder.CreateUIToFP(lhs_val, llvm::Type::getFloatTy(TheContext), "booltmp");
    case LT:
      lhs_val = TheBuilder.CreateFCmpULT(lhs_val, rhs_val, "lttmp");
      return TheBuilder.CreateUIToFP(lhs_val, llvm::Type::getFloatTy(TheContext), "booltmp");
    case LTE:
      lhs_val = TheBuilder.CreateFCmpULE(lhs_val, rhs_val, "ltetmp");
      return TheBuilder.CreateUIToFP(lhs_val, llvm::Type::getFloatTy(TheContext), "booltmp");
  }
  std::cout << "binary opp case exit" << std::endl;
  return NULL;
}

//Helper function from example code
llvm::AllocaInst* generateEntryBlockAlloca(std::string& name) {
  llvm::Function* currFn =
    TheBuilder.GetInsertBlock()->getParent();
  llvm::IRBuilder<> tmpBuilder(&currFn->getEntryBlock(),
    currFn->getEntryBlock().begin());

  return tmpBuilder.CreateAlloca(llvm::Type::getFloatTy(TheContext), 0, name.c_str());
}

//Helper function from example code
llvm::Value* assignmentStatement(std::string& lhs, llvm::Value* rhs) {
  if (!rhs) {
    return NULL;
  }
  if (!TheSymbolTable.count(lhs)) {
    // Allocate lhs
    TheSymbolTable[lhs] = generateEntryBlockAlloca(lhs);
  }
  return TheBuilder.CreateStore(rhs, TheSymbolTable[lhs]);
}

//Assignment statement to generate IR
llvm::Value* ASTAssignmentStatement::generateIR() const {
  std::string lhs_name(*this->lhs->name);
  llvm::Value* rhs_val = this->rhs->generateIR();
  return assignmentStatement(lhs_name, rhs_val);
}

//Generalized block to gerenate IR
llvm::Value* ASTBlock::generateIR() const {
  //llvm::Function* currFn = TheBuilder.GetInsertBlock()->getParent();
  for (int i = 0; i < this->statements.size(); i++) {
    this->statements[i]->generateIR();
  }
  return TheBuilder.GetInsertBlock()->getParent();
  //Figure this out later
}

//Unfinished - extra credit
llvm::Value* ASTIfStatement::generateIR() const {
  return NULL;
}

//Unfinished -extra credit
llvm::Value* ASTWhileStatement::generateIR() const {
    return NULL;
}

//Unfinished - extra credit
llvm::Value* ASTBreakStatement::generateIR() const {
    return NULL;
}


/*
 * Function to generate GraphView spec for the subtree rooted at any node in
 * an AST.
 *
 * @param node An AST node.
 *
 * @return Returns a string containing a complete GraphView specification to
 *   visualize the AST subtree rooted at node.
 */
std::string generateGVSpec(ASTNode* node) {
  std::string gvSpec = "digraph G {\n";
  std::string nodeName = "n0";
  node->generateGVSpec(nodeName, gvSpec);
  gvSpec += "}\n";
  return gvSpec;
}

/****************************************************************************
 **
 ** Below is the implementation of generateGVSpec() for each class defined in
 ** ast.hpp.  Please see the documentation in ast.hpp for a desription of
 ** this function.
 **
 ****************************************************************************/

void ASTIdentifier::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  gvSpec += "  " + nodeName
    + " [shape=box,label=\"Identifier: " + *this->name + "\"];\n";
}


void ASTFloat::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  gvSpec += "  " + nodeName
    + " [shape=box,label=\"Float: " + toString(this->value) + "\"];\n";
}


void ASTInteger::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  gvSpec += "  " + nodeName
    + " [shape=box,label=\"Integer: " + toString(this->value) + "\"];\n";
}


void ASTBoolean::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  gvSpec += "  " + nodeName
    + " [shape=box,label=\"Boolean: " + toString(this->value) + "\"];\n";
}


void ASTBinaryOperatorExpression::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  std::string lhsNodeName = nodeName + "_lhs";
  std::string rhsNodeName = nodeName + "_rhs";
  std::string opStr;
  switch (this->op) {
    case PLUS:
      opStr = "PLUS";
      break;
    case MINUS:
      opStr = "MINUS";
      break;
    case TIMES:
      opStr = "TIMES";
      break;
    case DIVIDEDBY:
      opStr = "DIVIDEDBY";
      break;
    case EQ:
      opStr = "EQ";
      break;
    case NEQ:
      opStr = "NEQ";
      break;
    case GT:
      opStr = "GT";
      break;
    case GTE:
      opStr = "GTE";
      break;
    case LT:
      opStr = "LT";
      break;
    case LTE:
      opStr = "LTE";
      break;
  }

  gvSpec += "  " + nodeName + " [label=\"" + opStr + "\"];\n";
  gvSpec += "  " + nodeName + " -> " + lhsNodeName + ";\n";
  this->lhs->generateGVSpec(lhsNodeName, gvSpec);
  gvSpec += "  " + nodeName + " -> " + rhsNodeName + ";\n";
  this->rhs->generateGVSpec(rhsNodeName, gvSpec);
}


void ASTAssignmentStatement::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  std::string lhsNodeName = nodeName + "_lhs";
  std::string rhsNodeName = nodeName + "_rhs";
  gvSpec += "  " + nodeName + " [label=\"Assignment\"];\n";
  gvSpec += "  " + nodeName + " -> " + lhsNodeName + ";\n";
  this->lhs->generateGVSpec(lhsNodeName, gvSpec);
  gvSpec += "  " + nodeName + " -> " + rhsNodeName + ";\n";
  this->rhs->generateGVSpec(rhsNodeName, gvSpec);
}


void ASTBlock::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  gvSpec += "  " + nodeName + " [label=\"Block\"];\n";
  for (int i = 0; i < this->statements.size(); i++) {
    std::string childNodeName = nodeName + "_" + toString(i);
    gvSpec += "  " + nodeName + " -> " + childNodeName + ";\n";
    this->statements[i]->generateGVSpec(childNodeName, gvSpec);
  }
}


void ASTIfStatement::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  std::string conditionNodeName = nodeName + "_cond";
  std::string ifBlockNodeName = nodeName + "_if";
  gvSpec += "  " + nodeName + " [label=\"If\"];\n";
  gvSpec += "  " + nodeName + " -> " + conditionNodeName + ";\n";
  this->condition->generateGVSpec(conditionNodeName, gvSpec);
  gvSpec += "  " + nodeName + " -> " + ifBlockNodeName + ";\n";
  this->ifBlock->generateGVSpec(ifBlockNodeName, gvSpec);

  if (this->elseBlock) {
    std::string elseBlockNodeName = nodeName + "_else";
    gvSpec += "  " + nodeName + " -> " + elseBlockNodeName + ";\n";
    this->elseBlock->generateGVSpec(elseBlockNodeName, gvSpec);
  }
}


void ASTWhileStatement::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  std::string conditionNodeName = nodeName + "_cond";
  std::string whileBlockNodeName = nodeName + "_while";
  gvSpec += "  " + nodeName + " [label=\"While\"];\n";
  gvSpec += "  " + nodeName + " -> " + conditionNodeName + ";\n";
  this->condition->generateGVSpec(conditionNodeName, gvSpec);
  gvSpec += "  " + nodeName + " -> " + whileBlockNodeName + ";\n";
  this->whileBlock->generateGVSpec(whileBlockNodeName, gvSpec);
}


void ASTBreakStatement::generateGVSpec(std::string nodeName, std::string& gvSpec) const {
  gvSpec += "  " + nodeName + " [label=\"Break\"];\n";
}
