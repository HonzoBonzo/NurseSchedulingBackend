#ifndef ACOEXCEPTION_H_
#define ACOEXCEPTION_H_

#include<stdexcept>

class ACOException : public std::runtime_error {
public:
	ACOException(const std::string& str) : std::runtime_error(str) {}
	ACOException(const char* str) : std::runtime_error(str) {}
};

#endif /* ACOEXCEPTION_H_ */