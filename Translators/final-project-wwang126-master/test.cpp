#include <iostream>

extern "C" {
    float py1();
}

int main(){
  std::cout << py1() << std::endl;
}
