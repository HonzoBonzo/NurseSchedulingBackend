using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public static class Helper
    {
        public static bool IsWeekend(int shift) {
            int value = shift % 28;
            if (value >= 20 && value < 28)
                return true;
            else
                return false;
        }

        public static int GetNumberOfShifts(Unit unit, int shift)
        {
            int count = 0;

            for (int i = 0; i < Constants.NURSE_NUMBER; i++)
            {
                if (unit.Array[i,shift])
                    count++;
            }
            return count;
        }

        public static int getShiftType(int shift)
        {
            return shift % 4;
        }

        public static bool isNightShift(int shift)
        {
            return (getShiftType(shift) == 3);
        }
    }
}
