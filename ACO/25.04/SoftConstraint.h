#ifndef SOFTCONSTRAINT_H_
#define SOFTCONSTRAINT_H_

#include "SoftConstraintException.h"
#include "ShiftIndex.h"

class SoftConstraint {
public:

	SoftConstraint() = default;
	SoftConstraint(char** solution, int no_employees, int no_days) : _solution{ solution }, _no_employees{ no_employees }, _no_weeks{ no_days / 7 }, _max_no_days{ no_days }, _no_days{ no_days }, _no_shifts{ 5 } {}

	/* 1. For the period of Friday 22:00 to Monday 0:00 a nurse should 
	      have either no shifts or at least 2 shifts (‘Complete Weekend’). */
	int f1(int);

	/* 2. For employees with availability of 30 ‐ 48 hours per week, the 
	      length of a series of night shifts should be within the range 2 ‐ 3. 
		  It could be before another series. */
	int f2(int);

	/* 3. For employees with availability of 30 ‐ 48 hours per week, within one 
	      week the numer of shifts is within the range 4 ‐ 5. */
	int f3(int);

	/* 4. For employees with availability of 30‐48 hours per week, the length of 
	      a series of shifts should be within the range of 4‐6. */
	int f4(int);

	/* 5. For all employees the length of a series of late shifts should be within 
	      the range of 2‐3. It could be within another series. */
	int f5(int);

	/* 6. An early shift after a day shift should be avoided. ([5] * Number of early shifts after days shifts) */
	int f6(int);

private:

	int _no_employees, _no_weeks, _no_days, _max_no_days, const _no_shifts;
	char **_solution;

};


#endif /* SOFTCONSTRAINT_H_ */