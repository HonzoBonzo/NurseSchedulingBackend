#ifndef ACO_H_
#define ACO_H_

#include <utility>

#include "ACOException.h"
#include "HardConstraint.h"
#include "SoftConstraint.h"

#include "Randoms.cpp"

/* Shift types */
#define EARLY	0
#define DAY		1
#define LATE	2
#define NIGHT	3
#define REST	4

/* Ant Colony Optimization for Nurse Scheduling Problem */
class ACO {
public:
	ACO(int no_ants, int no_days);
	virtual ~ACO();

	void run(int iterations);
	void initialize();

	inline double alpha(double a = -1) { if (a != -1) _alpha = a; return _alpha; }
	inline double beta(double b = -1) { if (b != -1) _beta = b; return _beta; }
	inline double evaporation(double r = -1) { if (r != -1) _ro = r; return _ro; }
	inline double tau_max(double tau = -1) { if (tau != -1) _taumax = tau; return _taumax; }
	inline double tau_min(double tau = -1) { if (tau != -1) _taumin = tau; return _taumin; }

	void print_solution();
	void print_pheromones();

private:

	void _route(int);
	char _roulette();

	int _hard_constraint_validation(int);
	int _cost(int);
	int _total_cost();

	void _make_connections();
	void _update_pheromones();
	double _calculate_probability(int, int, int, int);
	void _remember_best();
	bool _all_possibilities(char*);

	inline bool _are_connected(int, int);
	int _shift_r(int, int);
	void _shuffle_ants(int);
	
	void _clear_route(int);
	void _clear_routes();
	void _clear_probabilities();

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

	int _no_ants;
	int _no_days;
	int _no_shifts;

	double _alpha, _beta, _q, _ro, _taumin, _taumax;

	HardConstraint _hard_constraint, _hc;
	SoftConstraint _soft_constraint;
	Randoms *_random;

	int *_ants, *_costs;
	int _best_total_cost;
	double **_pheromones, **_delta_pheromones, **_heuristics;
	char *_graph, **_routes, **_solution, **_connections;
	std::pair<char, double> *_probabilities;

};

#endif