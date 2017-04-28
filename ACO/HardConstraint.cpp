#include "HardConstraint.h"
#include <iostream>

// 1. Cover needs to be fulfilled (i.e. no shifts must be left unassigned).
bool HardConstraint::cover_fulfilled(int shift) {
	int s = 0;

	if ((shift % _no_shifts) == REST) return true;

	for (int employee = 0; employee < _no_employees; ++employee)
		s += _solution[employee][shift];

	if (_night_shift_j(shift))
		return s == 1;
	else if (_working_day_j(shift))
		return s == 3;
	else if (_weekend_day_j(shift))
		return s == 2;

	return true;
}

// 2. For each day a nurse may start only one shift.
bool HardConstraint::only_one_shift_per_day(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0;

	for (int d = 0; d < _no_days; ++d) {
		tmp = &ptr[_d_j(d)];
		s = tmp[EARLY] + tmp[DAY] + tmp[LATE] + tmp[NIGHT];
		if (s > 1) return false;
	}

	return true;
}

// 3. Within a scheduling period a nurse is allowed to exceed the numer of hours 
//	  for which they are available for their department by at most 4 hours.
bool HardConstraint::exceed_hours(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0, sd = 0, sn = 0, k;
	int e36 = _no_weeks * 36, e32 = _no_weeks * 32, e20 = _no_weeks * 30;

	if (_no_weeks < 1) return true;

	for (int d = 0; (d < _no_days) && (d < 7 * _d_w(_no_days)); ++d) {
		k = _es_j(d);
		sd += ptr[k + EARLY] + ptr[k + DAY] + ptr[k + LATE];
		sn += ptr[k + NIGHT];
	}

	s = 8 * sd + 8 * sn;

	if (employee < 12) return (/*(e36 - 8) <= s &&*/ s <= (e36 + 4));
	else if (employee == 12) return (/*(e32 - 8) <= s &&*/ s <= (e32 + 4));
	else return (/*(e20 - 8) <= s &&*/ s <= (e20 + 4));
	
	return true;
}

// 4. The maximum numer of night shifts is 3 per period of 5 consecutive weeks.
bool HardConstraint::maximum_number_of_nights(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0;

	for (int d = 0; d < _no_days; ++d) 
		s += ptr[_ns_j(d)];

	if (s > 3) return false;

	return true;
}

// 5. A nurse must receive at least 2 weekends off duty per 5 week period. 
//	  A weekend off duty lasts 60 hours including Saturday 00:00 to Monday 04:00.
bool HardConstraint::weekends_off(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0;
	
	if (_no_weeks < 2) return true;

	for (int sat = 0; sat < _no_weeks; ++sat) {
		tmp = &ptr[_sat_j(sat)];
		s += tmp[REST] + tmp[5 + REST];
	}

	if (s < 4) return false; // at least 2 weekends * 2 rest
	
	return true;
}

// 6. Following a series of at least 2 consecutive night shifts a 42 hours rest is required.
bool HardConstraint::rest_hours_after_nights(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int sn = 0, sr = 0;

	for (int d = 0; d < _no_days; ++d) {
		sn = ptr[_ns_j(d)] + ptr[_ns_j((d + 1) % _max_no_days)];
		sr = ptr[_rs_j((d + 2) % _max_no_days)] + ptr[_rs_j((d + 3) % _max_no_days)];
		if (sn == 2 && sr != 2) return false;
	}

	return true;
}

// 7. During any period of 24 consecutive hours, at least 11 hours of rest is required.
bool HardConstraint::rest_hours_in_any_consecutive_24_period(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0, k1, k2;

	for (int d = 0; d < _no_days; ++d) {
		k1 = _es_j(d);
		k2 = _es_j((d + 1) % _max_no_days);
		s += (ptr[k1 + LATE] == 1 && ptr[k2 + EARLY] == 1)
			+ (ptr[k1 + LATE] == 1 && ptr[k2 + DAY] == 1)
			+ (ptr[k1 + NIGHT] == 1 && ptr[k2 + EARLY] == 1)
			+ (ptr[k1 + NIGHT] == 1 && ptr[k2 + DAY] == 1)
			+ (ptr[k1 + NIGHT] == 1 && ptr[k2 + LATE] == 1);
	}

	if (s > 0) return false;

	return true;
}

// 8. A night shift has to be followed by at least 14 hours rest. An exception 
//	  is that once in a period of 21 days for 24 consecutive hours, the resting
//    time may be reduced to 8 hours.
bool HardConstraint::rest_hours_following_night(int employee) {
	char *ptr = &_solution[employee][0], *tmp;

	//for (int d = 0; d < _no_days; ++d) { }

	return true;
}

// 9. The numer of consecutive night shifts is at most 3.
bool HardConstraint::consecutive_nights(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0;

	for (int d = 0; d < _no_days; ++d) {
		s = ptr[_ns_j(d)] + ptr[_ns_j((d + 1) % _max_no_days)] 
			+ ptr[_ns_j((d + 2) % _max_no_days)] + ptr[_ns_j((d + 3) % _max_no_days)];
		if (s > 3) return false;
	}

	return true;
}

// 10. The numer of consecutive shifts (workdays) is at most 6.
bool HardConstraint::consecutive_shifts(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int s = 0;

	for (int d = 0; d < _no_days - 6; ++d) {
		s = ptr[_rs_j(d)] + ptr[_rs_j((d + 1) % _max_no_days)] + ptr[_rs_j((d + 2) % _max_no_days)]
			+ ptr[_rs_j((d + 3) % _max_no_days)] + ptr[_rs_j((d + 4) % _max_no_days)]
			+ ptr[_rs_j((d + 5) % _max_no_days)] + ptr[_rs_j((d + 6) % _max_no_days)];
		if (s == 0) return false;
	}

	return true;
}