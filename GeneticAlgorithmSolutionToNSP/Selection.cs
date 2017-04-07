using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class Selection
    {
        public Selection() { }

        internal void makeSelection(Unit[] popArr)
        {
            sortPopulation(popArr);
        }

        private void sortPopulation(Unit[] populationArray) {
            quickSort(populationArray, 0, populationArray.Length - 1);
        }

        //nie wiem czy to dobrze dziala, nietestowane!!
        private static void quickSort(Unit[] array, int left, int right)
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
            if (left < j) quickSort(array, left, j);
            if (i < right) quickSort(array, i, right);
        }

    }
}
