#ifndef HARDCONSTRAINT_H_
#define HARDCONSTRAINT_H_

#include "HardConstraintException.h"
#include "ShiftIndex.h"

class HardConstraint {
public:

	HardConstraint() = default;
	HardConstraint(char** solution, int no_employees, int no_days) : _solution{ solution }, _no_employees{ no_employees }, _no_weeks{ no_days / 7 }, _max_no_days{no_days}, _no_days { no_days }, _no_shifts{ 5 } {}

	inline void set_days(int no_days) { _no_days = no_days; _no_weeks = _no_days / 7; }

	/* 1. Cover needs to be fulfilled (i.e. no shifts must be left unassigned). */
	bool cover_fulfilled(int);

	/* 2. For each day a nurse may start only one shift. */
	bool only_one_shift_per_day(int);

	/* 3. Within a scheduling period a nurse is allowed to exceed the numer of hours 
	      for which they are available for their department by at most 4 hours. */
	bool exceed_hours(int);

	/* 4. The maximum numer of night shifts is 3 per period of 5 consecutive weeks. */
	bool maximum_number_of_nights(int);

	/* 5. A nurse must receive at least 2 weekends off duty per 5 week period. 
	      A weekend off duty lasts 60 hours including Saturday 00:00 to Monday 04:00. */
	bool weekends_off(int);

	/* 6. Following a series of at least 2 consecutive night shifts a 42 hours rest is required. */
	bool rest_hours_after_nights(int);

	/* 7. During any period of 24 consecutive hours, at least 11 hours of rest is required. */
	bool rest_hours_in_any_consecutive_24_period(int);

	/* 8. A night shift has to be followed by at least 14 hours rest. An exception 
	      is that once in a period of 21 days for 24 consecutive hours, the resting 
		  time may be reduced to 8 hours. */
	bool rest_hours_following_night(int);

	/* 9. The numer of consecutive night shifts is at most 3. */
	bool consecutive_nights(int);

	/* 10. The numer of consecutive shifts (workdays) is at most 6. */
	bool consecutive_shifts(int);

private:

	int _no_employees, _no_weeks, _no_days, _max_no_days, _no_shifts;
	char **_solution;

};


#endif /* HARDCONSTRAINT_H_ */