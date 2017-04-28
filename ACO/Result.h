#ifndef RESULT_H_
#define RESULT_H_

#include "ShiftIndex.h"

class Result {
public:

	Result(int, int, int);

	bool import_from(const char*);
	bool export_into(const char*);

	char **begin(char **);
	void reserve();

private:

	int _no_employess, _no_days, _no_shifts;
	char **_result;

};

#endif