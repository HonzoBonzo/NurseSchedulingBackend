using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class Selection
    {
        public Selection() { }

        internal void MakeSelection(Unit[] popArr)
        {
            sortPopulation(popArr);
            selectGoodUnits(popArr);
        }

        private void selectGoodUnits(Unit[] popArr)
        {
            popArr = subArray(popArr, 0, Constants.POPULATION_SIZE);
        }

        private Unit[] subArray(Unit[] data, int startIndex, int length)
        {
            Unit[] result = new Unit[length];
            Array.Copy(data, startIndex, result, 0, length);
            return result;
        }

        private void sortPopulation(Unit[] populationArray) {
            quickSortHard(populationArray, 0, populationArray.Length - 1);
            quickSortSoft(populationArray, 0, populationArray.Length - 1);
        }

        //nie wiem czy to dobrze dziala, nietestowane!!
        private void quickSortHard(Unit[] array, int left, int right)
        {
            var i = left;
            var j = right;
            var pivot = array[(left + right) / 2].FailedHardConstraints;
            while (i < j)
            {
                while (array[i].FailedHardConstraints < pivot) i++;
                while (array[j].FailedHardConstraints > pivot) j--;
                if (i <= j)
                {
                    // swap
                    var tmp = array[i];
                    array[i++] = array[j];
                    array[j--] = tmp;
                }
            }
            if (left < j) quickSortHard(array, left, j);
            if (i < right) quickSortHard(array, i, right);
        }

        private void quickSortSoft(Unit[] array, int left, int right)
        {
            var i = left;
            var j = right;
            var pivot = array[(left + right) / 2].FailedSoftConstraints;
            while (i < j)
            {
                while (array[i].FailedSoftConstraints < pivot) i++;
                while (array[j].FailedSoftConstraints > pivot) j--;
                if (i <= j)
                {
                    // swap
                    var tmp = array[i];
                    array[i++] = array[j];
                    array[j--] = tmp;
                }
            }
            if (left < j) quickSortSoft(array, left, j);
            if (i < right) quickSortSoft(array, i, right);
        }

    }
}
