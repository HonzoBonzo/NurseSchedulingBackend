using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public static class Constants
    {
        public static int POPULATION_SIZE { get { return 100; }}
        public static int EPOCHS_NUMBER { get { return 100; }}
        public static int NURSE_NUMBER { get { return 16; }}
        public static int SHIFTS_NUMBER { get { return 252; }}
        public static int WEEKS_NUMBER { get { return 9; }}
        public static int WEEKS_TO_SCHEDULE { get { return 5; }}
        public static int DAY_SHIFT { get { return 0; }}
        public static int EARLY_SHIFT { get { return 1; }}
        public static int LATE_SHIFT { get { return 2; }}
        public static int NIGHT_SHIFT { get { return 3; }}
    }
}
