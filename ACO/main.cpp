#include <iostream>
#include <cstdlib>
#include <cmath>
#include <limits>
#include <climits>
#include <conio.h>
#include <time.h>

#include "ACO.h"

// if (ALPHA == 0) { stochastic search & sub-optimal route }
#define ALPHA			(double) 1.0

// if (BETA  == 0) { sub-optimal route }
#define BETA			(double) 1.0

// Pheromones evaporation. 
#define RO				(double) 0.01

// Minimum pheromone random number.
#define TAUMIN			(double) 1.0

// Maximum pheromone random number.
#define TAUMAX			(double) 1000.0

int main() {

	// Number of days to plan:
	int no_days = 7 * 5;
	// Number of employee:
	int no_ants = 16;
	// Maximum number of iterations:
	int iterations = 1000;

	try {
		// Create instance of ACO class:
		ACO aco = ACO(no_ants, no_days);
		
		// Setting parameters for ACO optimization:
		aco.alpha(ALPHA);
		aco.beta(BETA);
		aco.tau_min(TAUMIN);
		aco.tau_max(TAUMAX);
		aco.evaporation(RO);

		aco.min_cost(0);
		aco.max_duration(0*10 * 60); // 10 minutes

		// Make initialization:
		aco.initialize();
		aco.say_hello();

		// Prepare graph and pheromones matrix for optimization 
		// by reading results from file:
		//aco.prepare("data1.in");
		//aco.prepare("data2.in");
		//aco.prepare("data3.in");
		//aco.prepare("data4.in");

		//aco.print_solution();

		// Run ACO optimization:
		aco.run(iterations);
	} 
	catch (ACOException me) {
		std::cerr << me.what() << std::endl;
	}
	catch (...) {
		std::cerr << "Unknown error occurred" << std::endl;
	}

	std::cout << std::endl << "Push any button to close...";
	_getch();
	return 0;
}
