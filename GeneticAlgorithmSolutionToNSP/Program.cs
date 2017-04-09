using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{


    class Program
    {
        static void Main(string[] args)
        {
            NurseManager.initializeNurses();
            Selection sel = new Selection();
            Crossover cvr = new Crossover();
            Mutation mut = new Mutation();
            Population pop = new Population(sel, cvr, mut);

            pop.SimulateAlgorithm();

            Console.ReadKey();
        }
    }
}
