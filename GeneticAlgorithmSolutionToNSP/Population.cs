using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class Population : Unit
    {
        Unit[] arr;
        Selection selectionMethod;
        Crossover crossoverMethod;
        Mutation mutationMethod;

        public Population()
        {
            this.selectionMethod = new Selection();
            this.crossoverMethod = new Crossover();
            this.mutationMethod = new Mutation();
            this.arr = initPopulation();
        }

        public Population(Selection s, Crossover c, Mutation m)
        {
            this.selectionMethod = s;
            this.crossoverMethod = c;
            this.mutationMethod = m;
            this.arr = initPopulation();
        }

        public void SimulateAlgorithm() {
            for (int i = 0; i < Constants.EPOCHS_NUMBER; i++)
            {
                selectionMethod.MakeSelection(this.arr);
                crossoverMethod.MakeCrossover(this.arr);
                mutationMethod.makeMutation(this.arr);
            }
        }

        private Unit[] initPopulation()
        {
            return new Unit[Constants.POPULATION_SIZE];
        }

        public Unit[] Array { get { return this.arr; } }

    }
}
