#ifndef ACO_H_
#define ACO_H_

#include <utility>

#include "ACOException.h"
#include "HardConstraint.h"
#include "SoftConstraint.h"
#include "ShiftIndex.h"
#include "Result.h"
#include "Randoms.cpp"

/* Ant Colony Optimization for Nurse Scheduling Problem */
class ACO {
public:
	ACO(int, int);
	virtual ~ACO();

	void run(int iterations);
	void prepare(const char*);
	void initialize();
	void say_hello();

	inline double alpha(double a = -1) { if (a != -1) _alpha = a; return _alpha; }
	inline double beta(double b = -1) { if (b != -1) _beta = b; return _beta; }
	inline double evaporation(double r = -1) { if (r != -1) _ro = r; return _ro; }
	inline double tau_max(double tau = -1) { if (tau != -1) _taumax = tau; return _taumax; }
	inline double tau_min(double tau = -1) { if (tau != -1) _taumin = tau; return _taumin; }
	
	inline int max_duration(int duration = -1) { if (duration >= 0) _maximum_duration = duration; return _maximum_duration; }
	inline int min_cost(int cost = 0) { _minimum_cost = cost; return _minimum_cost; }

	void print_solution();
	void print_pheromones();
	void count_hours();
	void count_weekends();

private:

	void _route(int, int, int);
	char _roulette();

	int _hard_constraint_validation();
	int _check_results(char**);
	int _allocation_valid(int);
	int _shifts_per_week_valid(int, int);
	int _enough_shifts_per_week(int, int);
	int _cost(int);
	int _total_cost();

	void _make_connections();
	void _update_pheromones();
	void _update_allocations(int);
	double _heuristic(int);
	double _calculate_probability(int, int, int, int);
	void _remember_best();
	bool _all_possibilities(char*);
	void _update_used_ants(int);
	void _repair_day(int);

	void _select_weekends();
	void _select_weekends(int);
	void _clear_weekends(int);

	int _weekend_allocation_valid();
	void _update_shifts_per_week();
	void _update_shifts_per_week(int);

	void _select_correct_shift_distribution();
	bool _is_correct_shift_distribution();

	/*void _select_ants_for_weekend();
	bool _correct_weekend_selection();
	bool _has_weekend_off(int);*/

	void _shuffle_days();

	inline bool _are_connected(int, int);
	int _shift_r(int, int);
	void _shuffle_ants(int);

	int _D(int);
	int _d(int);
	
	void _clear_route(int);
	void _clear_routes();
	void _clear_day(int);
	void _clear_last_days(int, int);
	void _clear_to_last_day_of_week(int, int);
	void _clear_shifts_per_week();
	void _clear_shifts_per_week(int);
	void _clear_shift(int);
	void _clear_probabilities();
	void _clear_allocations();
	void _clear_used_ants();

	void _initialize();
	void _initialize_graph();
	void _initialize_connections();
	void _initialize_pheromones();
	void _initialize_heuristics();
	void _initialize_solution();
	void _initialize_routes();
	void _initialize_probabilities();
	void _initialize_ants();
	void _initialize_costs();
	void _initialize_allocations();
	void _initialize_used_ants();
	void _initialize_days();

	void _deallocate();
	void _deallocate_graph();
	void _deallocate_connections();
	void _deallocate_pheromones();
	void _deallocate_heuristics();
	void _deallocate_solution();
	void _deallocate_routes();
	void _deallocate_probabilities();
	void _deallocate_ants();
	void _deallocate_costs();
	void _deallocate_allocations();
	void _deallocate_used_ants();

	int _no_ants;
	int _no_days;
	int _no_shifts;

	int _curr_day;

	double _alpha, _beta, _q, _ro, _taumin, _taumax;
	int _minimum_cost, _maximum_duration;

	HardConstraint *_hard_constraint, _hc;
	SoftConstraint *_soft_constraint;
	Randoms *_random;
	Result *_result;

	int *_ants, *_costs, *_allocations, *_used_ants;
	int _best_total_cost;
	double **_pheromones, **_delta_pheromones, **_heuristics;
	char *_graph, **_routes, **_solution, **_connections, _selected_weekends[16], _shifts_per_week[16][5];
	std::pair<char, double> *_probabilities;
	char _weekends[10][2] = { { 1,2 },{ 1,3 },{ 1,4 },{ 1,5 },{ 2,3 },{ 2,4 },{ 2,5 },{ 3,4 },{ 3,5 },{ 4,5 } };

	int *_shift_distribution[16];
	
	int _weekend_count[16];
	int _weekend_ants[7];
	int _days[35];

	int d1_12[10][5] = { {5,5,5,4,4}, {5,5,4,5,4}, {5,4,5,5,4}, {4,5,5,5,4}, {4,5,5,4,5}, {4,5,4,5,5}, {4,4,5,5,5}, {5,4,5,4,5}, {5,4,4,5,5}, {5,5,4,4,5} };
	int d13[1][5] = { {4,4,4,4,4} };
	int d14_16[10][5] = { {3,3,3,2,2}, {3,3,2,3,2}, {3,2,3,3,2}, {2,3,3,3,2}, {2,3,3,2,3}, {2,3,2,3,3}, {2,2,3,3,3}, {3,2,3,2,3}, {3,2,2,3,3}, {3,3,2,2,3} };

};

#endif