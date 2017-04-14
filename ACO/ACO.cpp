#include <iostream>
#include <iomanip>
#include <stdlib.h>
#include <limits>
#include <time.h>

#include "ACO.h"
#include "ACOException.h"

ACO::ACO(int no_ants, int no_days) : _no_ants{ no_ants }, _no_days{no_days}, _no_shifts { 5 } {
	long t = time(NULL);
	srand(t);
	_random = new Randoms(t);
	_best_total_cost = std::numeric_limits<int>::max();
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
	uint64_t _t;
	int h = 0;

	for (int it = 1; it <= iterations; ++it) {

		std::cout << "Iteration [" << it << "]" << std::endl;
		// Ants should not go in sequence:
		_shuffle_ants(3);

		for (int ant = 0; ant < _no_ants; ++ant) {
			_t = 1;
			std::cout << "Realease [" << _ants[ant] << "] ant" << std::endl;

			// Run through graph until all hard constraints are valid:
			do {
				
				++_t;
				// Clear route path for current ant:
				_clear_route(_ants[ant]);
				
				// Go through graph again:
				_route(_ants[ant]);

				//std::cout << "hards: " << h << "                                                \r" << std::flush;

			} while ((h = _hard_constraint_validation(_ants[ant])) != 0);
		}

		print_solution();
		print_pheromones();

		// Update pheromones structure:
		_update_pheromones();

		// Remember the best result for ant:
		_remember_best();

		// Clears routes for all ant colony:
		_clear_routes();
	}
	return;
}

void ACO::initialize() {
	_initialize();
	_make_connections();
	_hard_constraint = HardConstraint(_routes, _no_ants, _no_days);
	_soft_constraint = SoftConstraint(_routes, _no_ants, _no_days);
}

void ACO::_route(int ant) {
	int shift, count, r = 0, h = 0;
	_routes[ant][rand() % 5] = 1;
	char p[5] = { 0, 0, 0, 0, 0 };

	for (int d = 0; d < _no_days - 1; ++d) {

		shift = _shift_r(ant, d);
		count = 0;
		for (int j = 0; j < _no_shifts; ++j) {
			if (_are_connected(shift, j)) {
				_probabilities[count].first = char(j);
				_probabilities[count].second = _calculate_probability(d, shift, j, ant);
				++count;
			}
		}

		// Choose shift until all hard constarins (to next day) will be valid:
		r = 0;
		//do {

			// clear previous choice:
			_routes[ant][_no_shifts*(d + 1) + r] = 0;

			// if all possibilities has been checked then run again:
			/*if (_all_possibilities(p)) {
				std::memset((void*)p, 0, 5*sizeof(char));
				_clear_route(ant);
				_routes[ant][rand() % 5] = 1;
				d = -1; // start with first day
				break; // all possibilities has been checked
			}*/

			// Choose shift using roulette method:
			r = _roulette();
			p[r] = 1;
			
			// If this pair is not connected, then go to another:
			while (!_are_connected(shift, r))
				r = _roulette();

			//if (!_are_connected(shift, r))
			//	continue;

			// Set choosen shift:
			_routes[ant][_no_shifts*(d + 1) + r] = 1;

			// Set amount of days:
			//_hard_constraint.set_days(d + 2);

			std::cout << "day [" << d << "]   \r";

		//} while ((h = _hard_constraint_validation(ant)) != 0);
		
		//_clear_probabilities();
	}

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

int ACO::_hard_constraint_validation(int ant) {
	int s = 0;
	int day = 0;
	//s += _hard_constraint.cover_fulfilled(ant);

	s += _hard_constraint.only_one_shift_per_day(ant);
	s += _hard_constraint.exceed_hours(ant);
	s += _hard_constraint.maximum_number_of_nights(ant);
	s += _hard_constraint.weekends_off(ant);

	//s += _hard_constraint.rest_hours_after_nights(ant);
	s += _hard_constraint.rest_hours_in_any_consecutive_24_period(ant);
	s += _hard_constraint.rest_hours_following_night(ant);

	s += _hard_constraint.consecutive_nights(ant);
	s += _hard_constraint.consecutive_shifts(ant);
	return (8 - s);
}

int ACO::_cost(int ant) {
	return (1000 * _soft_constraint.f1(ant) 
		+ 1000 * _soft_constraint.f2(ant) 
		+ 10 * _soft_constraint.f3(ant)
		+ 10 * _soft_constraint.f4(ant)
		+ 10 * _soft_constraint.f5(ant)
		+ 5 * _soft_constraint.f6(ant));
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
		std::cout << "Cost: " << cost << std::endl;
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

double ACO::_calculate_probability(int d, int shift1, int shift2, int ant) {
	double tau_ij, eta_ij, t, e, sum;

	tau_ij = pow(_pheromones[shift1][_no_shifts * d + shift2], _alpha);
	eta_ij = 0.1; // pow(rand() % 10, _beta);

	sum = 0.;
	for (int s = 0; s < _no_shifts; ++s) {
		if (_are_connected(shift1, s)) {
			t = pow(_pheromones[shift1][5 * d + s], _alpha);
			e = 0.1; // pow(rand() % 20, _beta);
			sum += t * e;
		}
	}
	return (tau_ij * eta_ij) / sum;
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

void ACO::_initialize() {
	// Initialize and make graph:
	_initialize_graph();
	// Initialize connections:
	_initialize_connections();
	// Initialize pheromone structure:
	_initialize_pheromones();
	// Initialize heuristics structure:
	_initialize_heuristics();
	// Initialize solution structure:
	_initialize_solution();
	// Initialize routes structure:
	_initialize_routes();
	_initialize_probabilities();
	_initialize_ants();
	_initialize_costs();
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
	_deallocate_costs();
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
			_heuristics[shift] = new double[whole_shifts] {};
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

void ACO::_clear_probabilities() {
	for (int s = 0; s < _no_shifts; ++s) {
		_probabilities[s].first = 0;
		_probabilities[s].second = 0.;
	}
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