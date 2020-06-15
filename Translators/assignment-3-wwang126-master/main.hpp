#include <iostream>
#include <set>
#include <vector>

extern int globalId;
struct Node
{
  int id;
  std::vector<Node*> children;
  std::string type;
  std::string data;
  Node(std::string typeIn, std::string dataIn)
  {
    id = globalId++;
    type = typeIn;
    data = dataIn;
  }
};
