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

        public static int SHIFTS_NUMBER { get { return 140; } }
        public static int SHIFTS_TO_SCHEDULE_NUMBER { get { return 140; } }
        public static int LAST_IMPORTED_SHIFT_INDEX { get { return 111; } }
        public static int FIRST_SHIFT_TO_SCHEDULE_INDEX { get { return 0; } }
        public static int LAST_SHIFT_TO_SCHEDULE_INDEX { get { return 139; } }

        public static int WEEKS_NUMBER { get { return 5; }}
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

        public static int UNIT_MUTATION_NUMBER { get { return (int)(UNIT_MUTATION_RATIO * SHIFTS_TO_SCHEDULE_NUMBER); } }
        public static int POPULATION_MUTATION_NUMBER { get { return (int)(POPULATION_MUTATION_RATIO * SHIFTS_TO_SCHEDULE_NUMBER); } }

        public static int FIRST_HALF_SHIFTS_TO_SCHEDULE_FIRST_INDEX { get { return FIRST_SHIFT_TO_SCHEDULE_INDEX; } }
        public static int FIRST_HALF_SHIFTS_TO_SCHEDULE_LAST_INDEX { get { return FIRST_SHIFT_TO_SCHEDULE_INDEX + POPULATION_SIZE; } }
        public static int SECOND_HALF_SHIFTS_TO_SCHEDULE_FIRST_INDEX { get { return LAST_SHIFT_TO_SCHEDULE_INDEX - POPULATION_SIZE; } }
        public static int SECOND_HALF_SHIFTS_TO_SCHEDULE_LAST_INDEX { get { return LAST_SHIFT_TO_SCHEDULE_INDEX; } }
    }
}
