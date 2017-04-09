using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class Population 
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
                Console.WriteLine("Generacja nr: {0}", i+1);
                selectionMethod.MakeSelection(this.arr);
                crossoverMethod.MakeCrossover(this.arr);
                mutationMethod.MakeMutation(this.arr);
            }
        }

        private Unit[] initPopulation()
        {
            Unit[] arr = new Unit[Constants.POPULATION_SIZE];

            for (int i = 0; i < arr.Length; i++)
            {
                arr[i] = new Unit();
                arr[i].Init();
            }

            return arr;
        }

        public Unit[] Array { get { return this.arr; } }

    }
}
