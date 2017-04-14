#include <iostream>
#include <cstdlib>
#include <cmath>
#include <limits>
#include <climits>
#include <conio.h>

#include "ACO.h"

// if (ALPHA == 0) { stochastic search & sub-optimal route }
#define ALPHA			(double) 1.0

// if (BETA  == 0) { sub-optimal route }
#define BETA			(double) 0.2

// Pheromones evaporation. 
#define RO				(double) 0.1

// Minimum pheromone random number.
#define TAUMIN			(double) 0.001

// Maximum pheromone random number.
#define TAUMAX			(double) 10.0

int main() {

	int no_days = 7 * 2;
	int no_ants = 12;
	int no_shifts = 5;
	int iterations = 15;

	try {
		// Create instance of ACO class:
		ACO aco = ACO(no_ants, no_days);
		
		aco.alpha(ALPHA);
		aco.beta(BETA);
		aco.tau_min(TAUMIN);
		aco.tau_max(TAUMAX);
		aco.evaporation(RO);

		aco.initialize();

		// Run ACO:
		aco.run(iterations);

		std::cout << std::endl << "Program has ended optimisation." << std::endl;
	} 
	catch (ACOException me) {
		std::cerr << me.what() << std::endl;
	}
	catch (...) {
		std::cerr << "Unknown error occurred" << std::endl;
	}

	_getch();
	return 0;
}
