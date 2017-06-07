#ifndef RESULT_H_
#define RESULT_H_

#include "ShiftIndex.h"

class Result {
public:

	Result(int, int, int);

	bool import_from(const char*);
	bool export_into(const char*);
	bool import_one_week(const char*);

	bool log(const char*,int it, double td, double id, int tc, int bc);

	char **begin(char **);
	void reserve();

private:

	int _no_employess, _no_days, _no_shifts;
	char **_result;

};

#endif