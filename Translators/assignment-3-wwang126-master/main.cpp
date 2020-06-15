#include "main.hpp"
#include <iostream>
#include <set>
#include "parser.hpp"


extern int yylex();

extern Node* target_program;
extern std::set<std::string> symbols;
extern int globalId = 0;

int printNode(Node* node){
  //if empty simply exit
  if (node->data.compare("") == 0){
    return 1;
  }
  //print information
  if (node->type.compare("") == 0){
    std::cout << node->id << "[label=\"" << node->data << "\"];" << std::endl;
  }
  else{
    std::cout << node->id << "[shape=" << node->type <<",label=\"" << node->data << "\"];" << std::endl;
  }
  //iterate through the child nodes and print them too
  for(std::vector<Node*>::iterator it = node->children.begin(); it != node->children.end(); ++it){
    if(printNode(*it) == 0){
      //print connection
      std::cout << node->id << "->" << (*it)->id << std::endl;
    }
  }
  return 0;

}

int main() {
  if (!yylex()) {
    std::cout << "digraph G {" << std::endl;
    printNode(target_program);
    std::cout << "}" << std::endl;
  }
}
