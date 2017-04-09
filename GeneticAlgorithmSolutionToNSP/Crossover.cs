using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class Crossover
    {
        public Crossover() { }

        internal void MakeCrossover(Unit[] unit)
        {
            this.consecutiveTwoHalfsMethod(unit);
        }

        private void consecutiveTwoHalfsMethod(Unit[] units)
        {
            int populationLen = units.Length;
            int numberOfPairs = populationLen / 2;
            units = Array.Resize(ref units, populationLen + numberOfPairs);
            

            for (int i = 0; i < numberOfPairs; i++)
            {
                units[populationLen + i] = this.makeChildConsecutiveTwoHalfsMethod(units[i * 2], units[i * 2 + 1]);
            }
        }

        private Unit makeChildConsecutiveTwoHalfsMethod(Unit father, Unit mother)
        { 
            Unit child = new Unit();

            for (int i = Constants.FIRST_HALF_SHIFTS_TO_SCHEDULE_FIRST_INDEX; i <= Constants.FIRST_HALF_SHIFTS_TO_SCHEDULE_LAST_INDEX; i++)
            {
                for (int j = 0; j < Constants.NURSE_NUMBER; j++)
                {
                    child.CopyShift(j, i, father); 
                }
            }

            for (int k = Constants.SECOND_HALF_SHIFTS_TO_SCHEDULE_FIRST_INDEX; k <= Constants.SECOND_HALF_SHIFTS_TO_SCHEDULE_LAST_INDEX; k++)
            {
                for (int j = 0; j < Constants.NURSE_NUMBER; j++)
                {
                    child.CopyShift(j, k, mother);
                }
            }


            return child;
        }
    }
}
