using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class Population : Unit
    {
        List<Unit> population;
        Selection selectionMethod;
        Crossover crossoverMethod;
        Mutation mutationMethod;

        public Population()
        {
            this.selectionMethod = new Selection();
            this.crossoverMethod = new Crossover();
            this.mutationMethod = new Mutation();
            this.population = initPopulation();
        }

        public Population(Selection s, Crossover c, Mutation m)
        {
            this.selectionMethod = s;
            this.crossoverMethod = c;
            this.mutationMethod = m;
            this.population = initPopulation();
        }

        public void SimulateAlgorithm() {
            for (int i = 0; i < Constants.EPOCHS_NUMBER; i++)
            {
                simulateEpoch();
            }
        }

        private List<Unit> initPopulation()
        {
            return new List<Unit>(Constants.POPULATION_SIZE);
        }

        private void simulateEpoch() {
            selectionMethod.makeSelection(this.population);
            crossoverMethod.makeCrossover(this.population);
            mutationMethod.makeMutation(this.population);
        }

    }
}
