#ifndef SOFTCONSTRAINTEXCEPTION_H_
#define SOFTCONSTRAINTEXCEPTION_H_

#include<stdexcept>

class SoftConstraintException : public std::runtime_error {
public:
	SoftConstraintException(const std::string& str) : std::runtime_error(str) {}
	SoftConstraintException(const char* str) : std::runtime_error(str) {}
};

#endif /* SOFTCONSTRAINTEXCEPTION_H_ */