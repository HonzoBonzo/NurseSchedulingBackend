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
        int failedHardConstraints;
        int failedSoftConstraints;


        public Unit()
        {
            importWeeks();
            makeChromosome();
            rateStrength();
        }

        void importWeeks()
        {

            for (int i = 0; i < Constants.NURSE_NUMBER; i++)
            {
                for (int j = 0; j <= Constants.LAST_IMPORTED_SHIFT_INDEX; j++)
                {
                    Random randomGen = new Random();
                    bool val = randomGen.Next(2) == 1 ? true : false;
                    this.array[i, j] = val;
                }
            }
        }

        void makeChromosome(){
            for (int i = 0; i < Constants.NURSE_NUMBER; i++)
            {
                for (int j = Constants.FIRST_SHIFT_TO_SCHEDULE_INDEX; j < Constants.SHIFTS_NUMBER; j++)
                {
                    Random randomGen = new Random();
                    bool val = randomGen.Next(2) == 1 ? true : false;
                    this.array[i, j] = val;
                }
            }
        }

        void rateStrength()
        {
            HardConstraints hard = new HardConstraints(this);
            this.failedHardConstraints = hard.Failed;

            SoftConstraints soft = new SoftConstraints(this);
            this.failedSoftConstraints = soft.Failed;
        }

        public Unit Mutate(float ratio)
        {
            int numberToMutate = (int)(ratio * Constants.FIELDS_NUMBER);
            
            while (numberToMutate != 0)
            {
                Random randGen = new Random();
                int randomNurse = randGen.Next(Constants.NURSE_NUMBER);
                int randomShift = randGen.Next(Constants.SHIFTS_NUMBER) + Constants.FIRST_SHIFT_TO_SCHEDULE_INDEX;
                //wiem ze moze sie ten sam zmutowac kilka razy, ale w czym to przeszkadza?
                this.array[randomNurse, randomShift] = !this.array[randomNurse, randomShift];
                numberToMutate--;
            }
            return this;
        }
    }


}