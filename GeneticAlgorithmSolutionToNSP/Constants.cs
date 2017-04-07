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

        public static int SHIFTS_NUMBER { get { return 252; } }
        public static int SHIFTS_TO_SCHEDULE_NUMBER { get { return 140; } }
        public static int LAST_IMPORTED_SHIFT_INDEX { get { return 111; } }
        public static int FIRST_SHIFT_TO_SCHEDULE_INDEX { get { return 112; } }

        public static int WEEKS_NUMBER { get { return 9; }}
        public static int WEEKS_TO_SCHEDULE_NUMBER { get { return 5; }}
        
        public static int DAY_SHIFT { get { return 0; }}
        public static int EARLY_SHIFT { get { return 1; }}
        public static int LATE_SHIFT { get { return 2; }}
        public static int NIGHT_SHIFT { get { return 3; }}
        public static int SHIFT_TYPE_NUMBER { get { return 4; } }

        public static int FIELDS_TO_SCHEDULE_NUMBER { get { return NURSE_NUMBER * SHIFTS_TO_SCHEDULE_NUMBER; }}
        /* proporcja wartosci true:false, gdzie dla true jest 0, a dla false 1-16 (w naszym przypadku) */
        public static int MAX_RANDOM_NUMBER_BOOL { 
            get {
                return (FIELDS_TO_SCHEDULE_NUMBER - SHIFTS_TO_SCHEDULE_NUMBER) / SHIFTS_TO_SCHEDULE_NUMBER + 1;
            } 
        }

        public static int UNIT_MUTATION_RATIO { get { return 1 / 5; } }
        public static int POPULATION_MUTATION_RATIO { get { return 1 / 5; } }

        public static int UNIT_MUTATION_NUMBER { get { return (int)(SHIFTS_TO_SCHEDULE_NUMBER); } }
        public static int POPULATION_MUTATION_NUMBER { get { return (int)(SHIFTS_TO_SCHEDULE_NUMBER); } }

    }
}
