using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class Mutation
    {
        public Mutation() { }

        internal void makeMutation(Unit[] unitArr)
        {
            for (int i = 0; i < Constants.POPULATION_MUTATION_NUMBER; i++)
            {
                unitArr[i].Mutate();
            }
        }
    }
}
