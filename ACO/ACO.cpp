#include <iostream>
#include <iomanip>
#include <stdlib.h>
#include <limits>
#include <time.h>

#include "ACO.h"
#include "ACOException.h"
#include "Result.h"

ACO::ACO(int no_ants, int no_days) : _no_ants{ no_ants }, _no_days{ no_days }, _no_shifts{ 5 } {
	long t = time(NULL);
	srand(t);
	_random = new Randoms(t);
	_best_total_cost = std::numeric_limits<int>::max();
	_maximum_duration = 0;
}

ACO::~ACO() {
	try {
		_deallocate();
	}
	catch (...) {
		throw ACOException("Deallocation error");
	}
}

void ACO::run(int iterations) {
	clock_t clocks, clocks_it;
	int h = 0, p, k, r, back, old, s, it;
	double td = 0;

	std::cout << "\n Maximum number of iterations: " << iterations << "\n"
		<< " Maximum duration: ";
	if (_maximum_duration == 0) std::cout << "unlimited\n";
	else std::cout << _maximum_duration << " [s]\n";
	std::cout << " Best cost level: " << _minimum_cost << "\n\n Run:\n";

	clocks_it = clock();
	for (it = 1; it <= iterations; ++it) {

		clocks = clock();
		s = 0;
		r = 0;

		// Clears routes for all ant colony:
		_clear_routes();

		// Select pairs of weekends off duty for ants:
		_select_weekends();

		for (int day = 0; day < _no_days; ++day) {

			// Set amount of days to check hard constraints:
			_hard_constraint->set_days(day + 1);


			_clear_shifts_per_week();

			do {
				p = 0;

				// Clear the allocation for current day:
				_clear_day(day);

				// Clear previously allocated ants:
				_clear_used_ants();

				// Ants should not go in sequence:
				_shuffle_ants(3);

				for (int shift = 0; shift < _no_shifts; ++shift) {
					k = 0;
					do {
						// Clear allocation for current shift:
						_clear_shift(_d_j(day) + shift);

						for (int ant = 0; ant < _no_ants; ++ant) {
							// Don't use previously used ants - only one shift per day:
							if (_used_ants[_ants[ant]] > -1) continue;
							// Go through graph:
							_route(_ants[ant], _d_j(day) + shift);
						}
						//std::cout << '#';

						// Deadlock break machanism:
						// Count 100 is experimentally selected.
						if (++k > 100) {
							p = 1;
							break;
						}

						// Check allocation for current shift.
					} while (_allocation_valid(_d_j(day) + shift) != 0);

					// If deadlock occurred then close day:
					if (p) break;

					// Signs allocated ants:
					_update_used_ants(_d_j(day) + shift);
				}

				// If deadlock occurred, then clear 3 last days
				// and allocate them again:
				if (p) {
					_clear_last_days(day, 1);
					day -= 2;
					back = day + 1;
					break;
				}

				/*if (!p && day != 0 && day % 7 == 0)
					for (int ant = 0; ant < _no_ants; ++ant)
						if (!_shifts_per_week_valid(ant, day)) {
							_clear_last_days(day, 3);
							old = day;
							p = 1;
							day -= 4;
							back = day + 1;
							break;
						}*/

				// If some ant exceed hours in period, then clear at most 2 days 
				// or clear allocation to last monday and allocate again:
				if(!p)
					for (int ant = 0; ant < _no_ants; ++ant)
						if (!_hard_constraint->exceed_hours(ant)) {
							int k = 0;
							do {
								if(k < 3)
									_clear_day(day - k);
							} while ((day - k++) % 7 != 0 && k < 3);

							p = 1;
							old = day;
							day -= k;
							back = day + 1;
							break;
						}

				// If some ant has to many consecutive shifts, then clear last 
				// day and allocate again:
				if(!p)
					for (int ant = 0; ant < _no_ants; ++ant)
						if (!_hard_constraint->consecutive_shifts(ant)) {
							_clear_last_days(day, 1);
							p = 1;
							old = day;
							day -= 2;
							back = day + 1;
							break;
						}

				// If some ant has too many nights, 
				// then clear last day and allocate again:
				if(!p)
					for (int ant = 0; ant < _no_ants; ++ant)
						if (!_hard_constraint->maximum_number_of_nights(ant)) {
							_clear_last_days(day, 1);
							p = 1;
							old = day;
							day -= 2;
							back = day + 1;
							break;
						}

				// If any of above hard constrain has failed and 
				// difference between current day and renew day 
				if (p) {
					if (old >= 6 && (old - back) <= 3 && (++s % 10) == 0) {
						_clear_last_days(day, 2);
						day -= 3;
						s = 0;
					}
					old = 0;
					back = 0;
					break;
				}

				// Print progress bar - the day since the allocation is valid:
				std::cout << std::setw(3) << day << " ";
				for (int d = 0; d < _no_days; ++d) {
					if (d <= day) std::cout << '#';
					else std::cout << ' ';
				}
				std::cout << "\r";

				// Now check rest of the hard constraints

			} while ((h = _hard_constraint_validation()) != 0);

			if (p) continue;
			

			_update_shifts_per_week();

			// Unallocated ants should have exactly one shift per day
			// If ant has no shift assigned, then assign rest shift:
			_repair_day(day);
		}

		// Update pheromones structure for future better performance:
		_update_pheromones();

		// Remember the best result:
		_remember_best();

		// Print statistics after allocation:
		td = (double(clock() - clocks_it) / double(CLOCKS_PER_SEC));
		std::cout << " [" << it << "] "
			<< "td: " << td << ", "
			<< "id: " << (double(clock() - clocks) / double(CLOCKS_PER_SEC)) << ", "
			<< "tc: " << _total_cost() << ", "
			<< "bc: " << _best_total_cost << "          " << std::endl;

		//print_solution();
		//print_pheromones();

		// Close if cost is zero - no further optimization needed:
		if (_best_total_cost <= _minimum_cost) break;
		// Close if duration approaches maximum optimization duration:
		if (_maximum_duration != 0 && td >= _maximum_duration) break;
	}

	// Print ending optimization statistics:
	std::cout << std::endl << " Program has ended optimization." << std::endl;
	std::cout << " Iterations:       " << it << std::endl
		      << " Best total cost:  " << _best_total_cost << std::endl
		      << " Total time:       " << (double(clock() - clocks_it) / double(CLOCKS_PER_SEC)) << " [s]" << std::endl;

	return;
}

void ACO::initialize() {
	_initialize();
	_make_connections();
	_hard_constraint = new HardConstraint(_routes, _no_ants, _no_days);
	_soft_constraint = new SoftConstraint(_routes, _no_ants, _no_days);
	_result = new Result(_no_ants, _no_days, _no_shifts);
}

void ACO::say_hello() {
	std::cout << "Ant Colony Optimization for Nurse Scheduling Problem." << std::endl << std::endl;

	std::cout << "Information format: \n"
		<< " [i] - iteration number,\n"
		<< " td - total duration since the beginning [s],\n"
		<< " id - duration of current iteration [s],\n"
		<< " tc - total cost for current iteration,\n"
		<< " bc - total best cost since the beginning.\n\n";
	return;
}

void ACO::prepare(const char* filename) {
	_clear_routes();
	_result->begin(_routes);
	if (!_result->import_from(filename))
		throw HardConstraintException("Unable to open input file.");

	std::cout << "Read file: " << filename << std::endl;
	std::cout << "Count of broken hard constraints for this results: " << _check_results(_routes) << std::endl;

	_update_pheromones();
	_remember_best();

	std::cout << " tc: " << _total_cost() << ", bc: " << _best_total_cost << std::endl << std::endl;
}

void ACO::_route(int ant, int shift) {
	int count, r, day, week, shift_p;

	day = _j_d(shift);
	week = _d_w(day) + 1;

	if (_enough_shifts_per_week(ant, day))
		return;

	// Don't allocate shift for selected pair of weekends:
	if (_weekend_day_d(day) && (_weekends[_selected_weekends[ant]][0] == week || _weekends[_selected_weekends[ant]][1] == week))
		return;

	// Make speacial allocation for first day:
	if (day == 0) {
		// Set random shift:
		r = rand() % _no_shifts;
		if(r == shift)
			_routes[ant][r] = 1;
		return;
	}

	// Select shift for previous day:
	shift_p = _shift_r(ant, day-1);

	// Calculate probabilities of choosing candidate vertices:
	count = 0;
	for (int j = 0; j < _no_shifts; ++j)
		if (_are_connected(shift_p, j)) {
			_probabilities[count].first = char(j);
			_probabilities[count].second = _calculate_probability(day, shift_p, j, ant);
			++count;
		}

	// Choose shift using roulette method:
	// If this pair is not connected, then go to another:
	do {
		r = _roulette();
	} while (!_are_connected(shift_p, r));

	// Set choosen shift:
	if(r == (shift % _no_shifts))
		_routes[ant][shift] = 1;

	return;
}

char ACO::_roulette() {
	double sum = 0., r = _random->Uniforme();
	int i = 0;
	do {
		sum += _probabilities[i].second;
		++i;
	} while (sum < r && i < _no_shifts);

	return _probabilities[i - 1].first;
}

int ACO::_hard_constraint_validation() {
	int s, whole_shifts = _no_days * _no_shifts;

	for (int ant = 0; ant < _no_ants; ++ant) {
		s = 0;
		//s += _hard_constraint->only_one_shift_per_day(ant);
		//s += _hard_constraint->exceed_hours(ant);
		//s += _hard_constraint->maximum_number_of_nights(ant);
		//s += _hard_constraint->weekends_off(ant);
		s += _hard_constraint->rest_hours_after_nights(ant);
		s += _hard_constraint->rest_hours_in_any_consecutive_24_period(ant);
		s += _hard_constraint->rest_hours_following_night(ant);
		s += _hard_constraint->consecutive_nights(ant);
		//s += _hard_constraint->consecutive_shifts(ant);
		if ((4 - s) != 0) return (4 - s);
	}

	return 0;
}

int ACO::_check_results(char **results) {
	int s, whole_shifts = _no_days * _no_shifts;
	s = 0;

	HardConstraint hard_constraint(results, _no_ants, _no_days);
	hard_constraint.set_days(_no_days);

	for (int shift = 0; shift < whole_shifts; ++shift)
		s += !hard_constraint.cover_fulfilled(shift);

	for (int ant = 0; ant < _no_ants; ++ant) {
		s += !hard_constraint.only_one_shift_per_day(ant);
		s += !hard_constraint.exceed_hours(ant);
		s += !hard_constraint.maximum_number_of_nights(ant);
		s += !hard_constraint.weekends_off(ant);
		s += !hard_constraint.rest_hours_after_nights(ant);
		s += !hard_constraint.rest_hours_in_any_consecutive_24_period(ant);
		s += !hard_constraint.rest_hours_following_night(ant);
		s += !hard_constraint.consecutive_nights(ant);
		s += !hard_constraint.consecutive_shifts(ant);
	}
	return s;
}

int ACO::_allocation_valid(int shift) {
	return !_hard_constraint->cover_fulfilled(shift);
}

int ACO::_shifts_per_week_valid(int ant, int day) {
	int week = _d_w(day);
	int shifts = _shifts_per_week[ant][week];
	if (ant < 12) return (shifts >= 4 && shifts <= 5);
	else if (ant == 12) return (shifts == 4);
	else return (shifts >= 2 && shifts <= 3);
}

int ACO::_enough_shifts_per_week(int ant, int day) {
	int week = _d_w(day);
	int shifts = _shifts_per_week[ant][week];
	if (ant < 12) return (shifts >= 5);
	else if (ant == 12) return (shifts >= 4);
	else return (shifts >= 3);
}

int ACO::_cost(int ant) {
	return (1000   * _soft_constraint->f1(ant)
			+ 1000 * _soft_constraint->f2(ant)
			+ 10   * _soft_constraint->f3(ant)
			+ 10   * _soft_constraint->f4(ant)
			+ 10   * _soft_constraint->f5(ant)
			+ 5	   * _soft_constraint->f6(ant));
}

int ACO::_total_cost() {
	int cost = 0;
	for (int ant = 0; ant < _no_ants; ++ant)
		cost += _cost(ant);
	return cost;
}

void ACO::_make_connections() {
	for (int i = 0; i < _no_shifts; ++i)
		for (int j = 0; j < _no_shifts; ++j) {
			_connections[i][j] = 1;
			if (i == LATE || i == NIGHT)
				if (j == EARLY || j == DAY || j == LATE)
					_connections[i][j] = 0;
		}
	return;
}

void ACO::_update_pheromones() {
	double path = 0.;
	int whole_shifts = _no_days * _no_shifts;
	int shift1, shift2, cost;

	// Calculate delta values:
	for (int ant = 0; ant < _no_ants; ++ant) {
		cost = _cost(ant);

		// Save the best cost for current ant:
		if (cost < _costs[ant]) _costs[ant] = cost;

		for (int d = 0; d < _no_days; ++d) {
			shift1 = _shift_r(ant, d);
			shift2 = _shift_r(ant, (d + 1) % _no_days);
			_delta_pheromones[shift1][_no_shifts * d + shift2] = 1. / (1. + cost - _costs[ant]);
		}
	}

	// Update with appropriate pattern:
	for (int shift = 0; shift < _no_shifts; ++shift)
		for (int d = 0; d < _no_days; ++d) {
			for (int j = 0; j < _no_shifts; ++j) {
				path = (1. - _ro) * _pheromones[shift][_no_shifts * d + j] + _delta_pheromones[shift][_no_shifts * d + j];
				if (path < _taumin)
					_pheromones[shift][_no_shifts * d + j] = _taumin;
				else if (path > _taumax)
					_pheromones[shift][_no_shifts * d + j] = _taumax;
				else
					_pheromones[shift][_no_shifts * d + j] = path;

				_delta_pheromones[shift][_no_shifts * d + j] = 0.;
			}
		}
}

void ACO::_update_allocations(int ant) {
	int whole_shifts = _no_shifts * _no_days;
	for (int shift = 0; shift < whole_shifts; ++shift) 
		if (_routes[ant][shift] == 1) 
			++_allocations[shift];
	return;
}

double ACO::_heuristic(int j) {
	return 1.0;/*(_D(j) - _d(j)) > 0 ? 1. : 0.;*/ //double(_D(j) - _d(j)) / double(_D(j));
}

double ACO::_calculate_probability(int d, int shift1, int shift2, int ant) {
	double tau_ij, eta_ij, t, e, sum;

	tau_ij = pow(_pheromones[shift1][_d_j(d) + shift2], _alpha);
	eta_ij = pow(_heuristic(_d_j(d) + shift2), _beta);

	sum = 0.;
	for (int s = 0; s < _no_shifts; ++s) {
		if (_are_connected(shift1, s)) {
			t = pow(_pheromones[shift1][_d_j(d) + s], _alpha);
			e = pow(_heuristic(_d_j(d) + s), _beta);
			sum += t * e;
		}
	}
	return sum == 0. ? 0. : (tau_ij * eta_ij) / sum;
}

void ACO::_remember_best() {
	int whole_shifts = _no_days * _no_shifts;
	int cost = _total_cost();
	if (cost < _best_total_cost) {
		// if result is better then prevoius one - change it
		_best_total_cost = cost;
		for (int ant = 0; ant < _no_ants; ++ant) {
			for (int shift = 0; shift < whole_shifts; ++shift)
				_solution[ant][shift] = _routes[ant][shift];
		}
	}
	return;
}

bool ACO::_all_possibilities(char* p) {
	for (int i = 0; i < 5; ++i)
		if (p[i] == 0) return false;
	return true;
}

void ACO::_update_used_ants(int shift) {
	for (int ant = 0; ant < _no_ants; ++ant) 
		if (_routes[ant][shift] == 1)
			_used_ants[ant] = shift % _no_shifts;
}

void ACO::_repair_day(int day) {
	int p ;
	for (int ant = 0; ant < _no_ants; ++ant) {
		p = 0;
		for (int shift = 0; shift < _no_shifts; ++shift) 
			if (_routes[ant][_d_j(day) + shift] == 1) p = 1;

		if (!p) _routes[ant][_rs_j(day)] = 1;
	}
}

void ACO::_select_weekends() {
	do {
		for (int ant = 0; ant < _no_ants; ++ant)
			_selected_weekends[ant] = rand() % 10;
	} while (!_weekend_allocation_valid());
}

int ACO::_weekend_allocation_valid() {
	int s = 0;
	for (int i = 1; i <= 5; ++i) {
		s = 0;
		for (int k = 0; k < 10; ++k) 
			if (_weekends[k][0] == i || _weekends[k][1] == i)
				for (int ant = 0; ant < _no_ants; ++ant) 
					if (_selected_weekends[ant] == k) 
						s++;
		if (s > 7) return 0;
	}
	return 1;
}

void ACO::_update_shifts_per_week() {
	for (int ant = 0; ant < _no_ants; ++ant)
		_update_shifts_per_week(ant);
	return;
}

void ACO::_update_shifts_per_week(int ant) {
	char *ptr = _routes[ant];
	int k;
	for (int day = 0; day < _no_days; ++day) {
		k = _es_j(day);
		if ((ptr[k + EARLY] + ptr[k + DAY] + ptr[k + LATE] + ptr[k + NIGHT]) == 1)
			_shifts_per_week[ant][_d_w(day)]++;
	}
	return;
}

bool ACO::_are_connected(int shift1, int shift2) {
	return ((shift1 >= EARLY && shift1 <= REST) && (shift2 >= EARLY && shift2 <= REST)) && _connections[shift1][shift2] == 1;
}

int ACO::_shift_r(int ant, int d) {
	for (int i = 0; i < _no_shifts; ++i)
		if (_routes[ant][_no_shifts * d + i] == 1)
			return i;
	return 0;
}

void ACO::_shuffle_ants(int times) {
	int ant1, ant2;
	for (int t = 0; t < times; ++t)
		for (int ant = 0; ant < _no_ants; ++ant) {
			ant1 = rand() % _no_ants;
			ant2 = rand() % _no_ants;
			std::swap(_ants[ant1], _ants[ant2]);
		}
}

int ACO::_D(int j) {
	if (_night_shift_j(j)) return 1;
	if (_working_day_j(j)) return 3;
	if (_weekend_day_j(j)) return 2;
}

int ACO::_d(int j) {
	return _allocations[j];
}

void ACO::_initialize() {
	_initialize_graph();
	_initialize_connections();
	_initialize_pheromones();
	_initialize_heuristics();
	_initialize_solution();
	_initialize_routes();
	_initialize_probabilities();
	_initialize_ants();
	_initialize_costs();
	_initialize_allocations();
	_initialize_used_ants();
	return;
}

void ACO::_deallocate() {
	_deallocate_graph();
	_deallocate_connections();
	_deallocate_pheromones();
	_deallocate_heuristics();
	_deallocate_solution();
	_deallocate_routes();
	_deallocate_probabilities();
	_deallocate_ants();
	_deallocate_allocations();
	_deallocate_used_ants();
	return;
}

void ACO::_initialize_graph() {
	try {
		// Alloacate graph:
		_graph = new char[_no_shifts] {};
		for (int shift = 0; shift < _no_shifts; ++shift)
			_graph[shift] = shift;
	}
	catch (std::bad_alloc) {
		throw ACOException("Graph allocation error");
	}
}

void ACO::_deallocate_graph() {
	delete[] _graph;
	_graph = nullptr;
	return;
}

void ACO::_initialize_connections() {
	try {
		// Alloacate connection matrix:
		_connections = new char*[_no_shifts] {nullptr};
		for (int shift = 0; shift < _no_shifts; ++shift)
			_connections[shift] = new char[_no_shifts] {0};
	}
	catch (std::bad_alloc) {
		throw ACOException("Connections allocation error");
	}
}

void ACO::_deallocate_connections() {
	for (int shift = 0; shift < _no_shifts; ++shift)
		delete[] _connections[shift];
	delete[] _connections;
	_connections = nullptr;
	return;
}

void ACO::_initialize_pheromones() {
	try {
		int whole_shifts = _no_days * _no_shifts;
		// Pheromones structures: alloacate row:
		_pheromones = new double*[_no_shifts] {nullptr};
		_delta_pheromones = new double*[_no_shifts] {nullptr};

		// Alloacate structures:
		for (int shift = 0; shift < _no_shifts; ++shift) {
			_pheromones[shift] = new double[whole_shifts] {};
			_delta_pheromones[shift] = new double[whole_shifts] {};
		}

		for (int shift = 0; shift < _no_shifts; ++shift)
			for (int j = 0; j < whole_shifts; ++j)
				_pheromones[shift][j] = /*_random->Uniforme() **/ _taumin;

	}
	catch (std::bad_alloc) {
		throw ACOException("Pheromones allocation error");
	}
}

void ACO::_deallocate_pheromones() {
	for (int shift = 0; shift < _no_shifts; ++shift) {
		delete[] _pheromones[shift];
		delete[] _delta_pheromones[shift];
	}
	delete[] _pheromones;
	delete[] _delta_pheromones;
	_pheromones = nullptr;
	_delta_pheromones = nullptr;
	return;
}

void ACO::_initialize_heuristics() {
	try {
		int whole_shifts = _no_days * _no_shifts;
		// Heuristics structure: alloacate row:
		_heuristics = new double*[_no_shifts] {nullptr};

		// Alloacate structures:
		for (int shift = 0; shift < _no_shifts; ++shift)
			_heuristics[shift] = new double[whole_shifts] {1.0};
	}
	catch (std::bad_alloc) {
		throw ACOException("Heuristics allocation error");
	}
	return;
}

void ACO::_deallocate_heuristics() {
	for (int shift = 0; shift < _no_shifts; ++shift)
		delete[] _heuristics[shift];
	delete[] _heuristics;
	_heuristics = nullptr;
	return;
}

void ACO::_initialize_solution() {
	try {
		int whole_shifts = _no_days * _no_shifts;
		// Solution structure: alloacate row:
		_solution = new char*[_no_ants] {nullptr};

		// Alloacate structures:
		for (int ant = 0; ant < _no_ants; ++ant)
			_solution[ant] = new char[whole_shifts] {0};
	}
	catch (std::bad_alloc) {
		throw ACOException("Solution allocation error");
	}
	return;
}

void ACO::_deallocate_solution() {
	for (int ant = 0; ant < _no_ants; ++ant)
		delete[] _solution[ant];
	delete[] _solution;
	_solution = nullptr;
	return;
}

void ACO::_initialize_routes() {
	try {
		int whole_shifts = _no_days * _no_shifts;
		// Route structures: alloacate ants row:
		_routes = new char*[_no_ants] {nullptr};

		// Alloacate structures:
		for (int ant = 0; ant < _no_ants; ++ant)
			_routes[ant] = new char[whole_shifts] {0};
	}
	catch (std::bad_alloc) {
		throw ACOException("Solution allocation error");
	}
	return;
}

void ACO::_deallocate_routes() {
	for (int ant = 0; ant < _no_ants; ++ant)
		delete[] _routes[ant];
	delete[] _routes;
	_routes = nullptr;
	return;
}

void ACO::_initialize_probabilities() {
	try {
		_probabilities = new std::pair<char, double>[_no_shifts]; //{std::make_pair<char, double>(0, 0.)};
		for (int s = 0; s < _no_shifts; ++s) {
			_probabilities[s].first = 0;
			_probabilities[s].second = 0.;
		}
	}
	catch (std::bad_alloc) {
		throw ACOException("Probabilities allocation error");
	}
	return;
}

void ACO::_deallocate_probabilities() {
	if(_probabilities)
		delete[] _probabilities;
	_probabilities = nullptr;
	return;
}

void ACO::_initialize_ants() {
	try {
		_ants = new int[_no_ants] {};
		for (int ant = 0; ant < _no_ants; ++ant)
			_ants[ant] = ant;
	}
	catch (std::bad_alloc) {
		throw ACOException("Ants allocation error");
	}
	return;
}

void ACO::_deallocate_ants() {
	if (_ants)
		delete[] _ants;
	_ants = nullptr;
	return;
}

void ACO::_initialize_costs() {
	try {
		_costs = new int[_no_ants] {std::numeric_limits<int>::max()};
	}
	catch (std::bad_alloc) {
		throw ACOException("Costs allocation error");
	}
	return;
}

void ACO::_deallocate_costs() {
	if (_costs)
		delete[] _costs;
	_costs = nullptr;
	return;
}

void ACO::_initialize_allocations() {
	try {
		int whole_shifts = _no_days * _no_shifts;
		_allocations = new int[whole_shifts] {0};
	}
	catch (std::bad_alloc) {
		throw ACOException("Employee table allocation error");
	}
	return;
}

void ACO::_initialize_used_ants() {
	try {
		_used_ants = new int[_no_ants] {-1};
	}
	catch (std::bad_alloc) {
		throw ACOException("Used ant table allocation error");
	}
	return;
}

void ACO::_deallocate_allocations() {
	if (_allocations)
		delete[] _allocations;
	_allocations = nullptr;
	return;
}

void ACO::_deallocate_used_ants() {
	if (_used_ants)
		delete[] _used_ants;
	_used_ants = nullptr;
	return;
}

void ACO::_clear_routes() {
	int whole_shifts = _no_days * _no_shifts;
	for (int ant = 0; ant < _no_ants; ++ant)
		for (int shift = 0; shift < whole_shifts; ++shift)
			_routes[ant][shift] = 0;
}

void ACO::_clear_route(int ant) {
	int whole_shifts = _no_days * _no_shifts;
	for (int shift = 0; shift < whole_shifts; ++shift)
		_routes[ant][shift] = 0;
	return;
}

void ACO::_clear_day(int day) {
	for (int ant = 0; ant < _no_ants; ++ant) 
		for (int shift = 0; shift < _no_shifts; ++shift) 
			_clear_shift(_es_j(day) + shift);
	return;
}

void ACO::_clear_last_days(int day, int count) {
	for (int k = 0; k <= count; ++k)
		_clear_day(day - k);
	return;
}

void ACO::_clear_shifts_per_week() {
	for (int ant = 0; ant < _no_ants; ++ant)
		_clear_shifts_per_week(ant);
	return;
}

void ACO::_clear_shifts_per_week(int ant) {
	for (int week = 0; week < _d_w(_no_days); ++week)
		_shifts_per_week[ant][week] = 0;
	return;
}

void ACO::_clear_shift(int shift){
	for (int ant = 0; ant < _no_ants; ++ant)
		_routes[ant][shift] = 0;
}

void ACO::_clear_probabilities() {
	for (int s = 0; s < _no_shifts; ++s) {
		_probabilities[s].first = 0;
		_probabilities[s].second = 0.;
	}
}

void ACO::_clear_allocations() {
	int whole_shifts = _no_days * _no_shifts;
	for (int shift = 0; shift < whole_shifts; ++shift)
		_allocations[shift] = 0;
	return;
}

void ACO::_clear_used_ants() {
	for (int ant = 0; ant < _no_ants; ++ant)
		_used_ants[ant] = -1;
	return;
}

void ACO::print_solution() {
	using std::cout;
	using std::endl;

	for (int ant = 0; ant < _no_ants; ++ant) {
		cout << ant << " |";
		for (int j = 0; j < (_no_shifts*_no_days); ++j) {
			cout << (int)_routes[ant][j];
			if (j % _no_shifts == (_no_shifts - 1)) cout << "|";
		}
		cout << endl;
	}
	return;
}

void ACO::print_pheromones() {
	using std::cout;
	using std::endl;

	cout.precision(2);
	for (int i = 0; i < _no_shifts; i++) {
		for (int j = 0; j < (_no_days * _no_shifts); ++j) {
			cout << std::setw(10) << _pheromones[i][j];
			if (j % _no_shifts == (_no_shifts - 1)) cout << std::setw(3) << " | ";
		}
		cout << endl;
	}
	return;
}