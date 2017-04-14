#ifndef HARDCONSTRAINTEXCEPTION_H_
#define HARDCONSTRAINTEXCEPTION_H_

#include<stdexcept>

class HardConstraintException : public std::runtime_error {
public:
	HardConstraintException(const std::string& str) : std::runtime_error(str) {}
	HardConstraintException(const char* str) : std::runtime_error(str) {}
};

#endif /* HARDCONSTRAINTEXCEPTION_H_ */