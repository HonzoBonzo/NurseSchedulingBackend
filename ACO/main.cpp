#include <iostream>
#include <cstdlib>
#include <cmath>
#include <limits>
#include <climits>
#include <conio.h>
#include <time.h>

#include "ACO.h"

// if (ALPHA == 0) { stochastic search & sub-optimal route }
#define ALPHA			(double) 0.8

// if (BETA  == 0) { sub-optimal route }
#define BETA			(double) 1.0

// Pheromones evaporation. 
#define RO				(double) 0.05

// Minimum pheromone random number.
#define TAUMIN			(double) 1.0

// Maximum pheromone random number.
#define TAUMAX			(double) 1000.0

int main(int argc, char** argv) {

	int iteration, duration, cost;
	const char* filename;

	iteration = 1000;
	duration = 0;
	cost = 0;
	filename = "firstWeek.txt";

	if (argc <= 1)
		std::cout << "Default argument values accepted." << std::endl;

	// Argument parsing:
	if (argc >= 2) {
		iteration = std::atoi(argv[1]);
		if (iteration <= 0) {
			std::cout << "Invalid iterations count!" << std::endl;
			return EXIT_FAILURE;
		}
	}

	if (argc >= 3) {
		duration = std::atoi(argv[2]);
		if (duration <= 0)
			std::cout << "Unlimited optimisation time." << std::endl;
	}

	if (argc >= 4) {
		cost = std::atoi(argv[3]);
		if(cost <= 0)
			std::cout << "Minimum cost level set to zero." << std::endl;
	}

	if (argc >= 5)
		filename = argv[4];
	

	// Number of days to plan:
	int no_days = 7 * 5;
	// Number of employee:
	int no_ants = 16;

	try {
		// Create instance of ACO class:
		ACO aco = ACO(no_ants, no_days);
		
		// Setting parameters for ACO optimization:
		aco.alpha(ALPHA);
		aco.beta(BETA);
		aco.tau_min(TAUMIN);
		aco.tau_max(TAUMAX);
		aco.evaporation(RO);

		aco.min_cost(cost);
		aco.max_duration(duration);
		aco.week_filename(filename);

		// Make initialization:
		aco.initialize();
		aco.say_hello();

		// Prepare graph and pheromones matrix for optimization 
		// by reading results from file:
		//aco.prepare("schedule-5000.txt");
		//aco.prepare("schedule-4000.txt");
		//aco.prepare("schedule-3000.txt");
		//aco.prepare("schedule-2000.txt");

		//aco.print_solution();

		// Run ACO optimization:
		aco.run(iteration);
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
