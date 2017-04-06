using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class Unit
    {
        bool[,] array = new bool[Constants.NURSE_NUMBER, Constants.SHIFTS_NUMBER];

        public Unit()
        {
            makeChromosome();
            rateStrength();
        }

        void makeChromosome(){
            
        }

        void rateStrength()
        {
            //odpal checkConstrainty
        }

        public Unit Mutate()
        {
            return this;
        }
    }


}