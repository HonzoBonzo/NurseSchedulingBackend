#include <stdexcept>
#include <fstream>
#include <iostream>
#include <string>
#include "Result.h"

Result::Result(int no_employees, int no_days, int no_shifts) : _no_employess{ no_employees }, _no_days{ no_days }, _no_shifts{no_shifts} {
	_result = nullptr;
}

bool Result::import_from(const char* filename) {
	std::ifstream file;
	std::string line;
	line.reserve(300);
	char *record = (char*)line.c_str();
	int employee;

	file.open(filename, std::ios::in);
	if (file.is_open()) {

		employee = 0;
		while (file.getline(record, 300) && employee < _no_employess) {
			//std::cout << record << std::endl;
			for (int d = 0; d < _no_days; ++d) {
				if (record[2 * (4 * d + EARLY)] == '1') _result[employee][5 * d + EARLY] = 1;
				else if (record[2 * (4 * d + DAY)] == '1') _result[employee][5 * d + DAY] = 1;
				else if (record[2 * (4 * d + LATE)] == '1') _result[employee][5 * d + LATE] = 1;
				else if (record[2 * (4 * d + NIGHT)] == '1') _result[employee][5 * d + NIGHT] = 1;
				else _result[employee][5 * d + REST] = 1;
			}
			++employee;
		}

		file.close();
		return true;
	}
	return false;
}

bool Result::export_into(const char* filename) {
	std::ofstream file;
	char day[4] = {0,0,0,0};
	int employee;

	file.open(filename, std::ios::out | std::ios::trunc);
	if (file.is_open()) {

		employee = 0;
		while (employee < _no_employess) {
			for (int d = 0; d < _no_days; ++d) {
				std::memset((void*)day, '0', 4);
				if (_result[employee][5 * d + EARLY] == 1) day[EARLY] = '1';
				else if (_result[employee][5 * d + DAY] == 1) day[DAY] = '1';
				else if (_result[employee][5 * d + LATE] == 1) day[LATE] = '1';
				else if (_result[employee][5 * d + NIGHT] == 1) day[NIGHT] = '1';
				file.write(day, 4);
			}
			file.put('\n');
			++employee;
		}

		file.close();
		return true;
	}
	return false;
}

char** Result::begin(char** result = nullptr) {
	if (result != nullptr) _result = result;
 	return _result;
}

void Result::reserve() {
	int whole_shifts = _no_days * _no_shifts;
	try {
		_result = new char*[_no_employess] {nullptr};
		for (int e = 0; e < _no_employess; ++e)
			_result[e] = new char[whole_shifts] {0};
	}
	catch (std::bad_alloc) {

	}
}