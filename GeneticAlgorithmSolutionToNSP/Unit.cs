using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class Unit //osobnik
    {
        bool[,] array = new bool[Constants.NURSE_NUMBER, Constants.SHIFTS_NUMBER];
        int failedHardConstraints;
        int failedSoftConstraintsWeight;
        Random randomGen = new Random();

        public Unit() { }

        public void Init() {
            //this.importWeeks(); //importuje poprzednie 4 tygodnie
            this.makeChromosome(); //
        }

        void importWeeks()
        {

            for (int i = 0; i < Constants.NURSE_NUMBER; i++)
            {
                for (int j = 0; j <= Constants.LAST_IMPORTED_SHIFT_INDEX; j++)
                {
                    this.array[i, j] = randBool(); //na razie wypelniamy czymkolwiek, bo nie mamy poprzedniej
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

        public bool randBool()
        {
            
            /* losujemy od 0 - 16 liczbe, dla 0 zwracamy true czyli x w naszym grafiku, false'ow jest duzo wiecej */
            int val = randomGen.Next(0,Constants.MAX_RANDOM_NUMBER_BOOL) ;
            return val == 0 ? true : false;
        }

        private int randNurse()
        {
            //Random randGen = new Random();
            return randomGen.Next(Constants.NURSE_NUMBER); ;
        }

        private int randShift()
        {
            //Random randGen = new Random();
            return randomGen.Next(Constants.SHIFTS_TO_SCHEDULE_NUMBER) + Constants.FIRST_SHIFT_TO_SCHEDULE_INDEX;
        }

        public Unit Mutate()
        {
            int numberToMutate = Constants.UNIT_MUTATION_NUMBER;
            
            while (numberToMutate != 0)
            {
                int randomNurse = randNurse();
                int randomShift = randShift();
                //wiem ze moze sie ten sam zmutowac kilka razy, ale w czym to przeszkadza?

                if (NurseCalculations.IsWeekend(randomShift) )
                {
                    if (!NurseCalculations.isNightShift(randomShift) && NurseCalculations.GetNumberOfShifts(this, randomShift) > 2)
                        continue;
                    if (NurseCalculations.isNightShift(randomShift) && NurseCalculations.GetNumberOfShifts(this, randomShift) > 1)
                        continue;
                }
                else
                {
                    if (!NurseCalculations.isNightShift(randomShift) && NurseCalculations.GetNumberOfShifts(this, randomShift) > 3)
                        continue;
                    if (NurseCalculations.isNightShift(randomShift) && NurseCalculations.GetNumberOfShifts(this, randomShift) > 1)
                        continue;
                }

                this.array[randomNurse, randomShift] = !this.array[randomNurse, randomShift];
                //todo wurzucic jedynki z sensem 
                
                

                numberToMutate--;
            }
            return this;
        }

        public void SetShift(int nurseIndex, int shiftIndex)
        {
            this.array[nurseIndex, shiftIndex] = true; 
        }

        public void CopyShift(int nurseIndex, int shiftIndex, Unit src)
        {
            this.array[nurseIndex, shiftIndex] = src.Array[nurseIndex, shiftIndex];
        }

        public int FailedHardConstraints { get { return this.failedHardConstraints; } set { this.failedHardConstraints = value; } }
        public int FailedSoftConstraints { get { return this.failedSoftConstraintsWeight; } set { this.failedSoftConstraintsWeight = value; } }
        public bool[,] Array { get { return this.array; } }
    }


}