#include <iostream>
#include <map>
#include <list>

extern int yylex();
extern bool _error;
extern std::map<std::string, float> symbols;
extern std::list<std::string> output;

int main() {
  return yylex();
  if (!yylex()){
    if (!_error) {
      std::cout << std::endl << "Test Values:" << std::endl;
      std::map<std::string, float>::iterator it;
      for (it = symbols.begin(); it != symbols.end(); it++){
        std::cout << it->first << " : " << it->second << std::endl;
      }
      /*print out list of code generated*/
      for(std::list<std::string>::iterator iter = output.begin(); iter != output.end(); iter++){
        std::cout<<*iter<<std::endl;
      }

    }
    return 0;
  } else {
    return 1;
  }
}
