﻿using System;
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
                    this.array[i, j] = randBool();
                }
            }
        }

        void rateStrength()
        {
            HardConstraints hard = new HardConstraints(this.array);
            this.failedHardConstraints = hard.Failed;

            SoftConstraints soft = new SoftConstraints(this.array);
            this.failedSoftConstraints = soft.Failed;
        }

        private bool randBool()
        {
            Random randomGen = new Random();
            /* losujemy od 0 - 16 liczbe, dla 0 zwracamy true czyli x w naszym grafiku, false'ow jest duzo wiecej */
            bool val = randomGen.Next(Constants.MAX_RANDOM_NUMBER_BOOL) == 0 ? true : false;
            return val;
        }

        private int randNurse()
        {
            Random randGen = new Random();
            return randGen.Next(Constants.NURSE_NUMBER); ;
        }

        private int randShift()
        {
            Random randGen = new Random();
            return randGen.Next(Constants.SHIFTS_TO_SCHEDULE_NUMBER) + Constants.FIRST_SHIFT_TO_SCHEDULE_INDEX;
        }

        public Unit Mutate()
        {
            int numberToMutate = Constants.UNIT_MUTATION_NUMBER;
            
            while (numberToMutate != 0)
            {
                int randomNurse = randNurse();
                int randomShift = randShift();
                //wiem ze moze sie ten sam zmutowac kilka razy, ale w czym to przeszkadza?
                this.array[randomNurse, randomShift] = !this.array[randomNurse, randomShift];
                numberToMutate--;
            }
            return this;
        }

        public int FailedHardConstraints { get { return this.failedHardConstraints; } }
        public int FailedSoftConstraints { get { return this.failedSoftConstraints; } }
    }


}