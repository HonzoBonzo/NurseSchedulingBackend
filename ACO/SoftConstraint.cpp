#include "SoftConstraint.h"

int SoftConstraint::f1(int employee) {

	return 0;
}

int SoftConstraint::f2(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int count = 0, n;

	if (employee > 12) return 0;

	for (int d = 0; d < _no_days - 2; ++d) {
		n = ptr[_ns_j(d)] + ptr[_ns_j(d + 1)] + ptr[_ns_j(d + 2)];
		if (n == 1) ++count;
	}

	return count;
}

int SoftConstraint::f3(int employee) {

	return 0;
}

int SoftConstraint::f4(int employee) {

	return 0;
}

int SoftConstraint::f5(int employee) {

	return 0;
}

// 6. An early shift after a day shift should be avoided. ([5] * Number of early shifts after days shifts)
int SoftConstraint::f6(int employee) {
	char *ptr = &_solution[employee][0], *tmp;
	int count = 0;

	for (int d = 0; d < _no_days; ++d)
		count += (ptr[_ds_j(d)] == 1 && ptr[_es_j((d + 1) % _max_no_days)] == 1);

	return count;
}