using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class HardConstraints : Constraint
    {
        private bool[,] arr;

        public int getFailed()
        {
            return this.failed;
        }

        public HardConstraints() : base(){}

        public HardConstraints(bool[,] p)
        {
            this.arr = p;

            for (int nurseId = 0; nurseId < Constants.NURSE_NUMBER; nurseId++)
            {
                for (int shift = 0; shift < Constants.SHIFTS_NUMBER; shift++)
                {
                    this.nurseId = nurseId;
                    this.shift = shift;
		            this.nurse = NurseManager.getNurse(nurseId);

                    if (nurse.totalWorkedTime > nurse.hoursPerWeek * 4)
                        this.failed++;

                    if (isNurseAlreadyWorkingToday())
                        this.failed++;
		            if(!isNumberOfNightShiftsLessOrEqualThanThree())
                        this.failed++;
		            if(!isNumberOfFreeWeekendsMoreOrEqualThenTwo())
                        this.failed++;
		            if(!enoughRestAfterConsecutiveNightShifts())
                        this.failed++;
		            if(!enoughRestIn24Hours())
                        this.failed++;
		            if(!enoughRestAfterNightShift())
                        this.failed++;
		            if(!consecutiveNightShiftsConstraint())
                        this.failed++;
		            if(!consecutiveWorkdaysConstraint())
                        this.failed++;
                }
            }


	    }

        /*
         * For each day a nurse may start only one shift
         */
        public bool isNurseAlreadyWorkingToday()
        {
            bool[] nurseScheduleForTheDay = getNurseDaySchedule(this.nurseId, this.shift);
            for (int i = 0; i < 4; i++)
            {
                if (nurseScheduleForTheDay[i])
                    return true;
            }

            return false;
        }

        /*
	     * The maximum number of night shifts is 3 per period of 5 consecutive
	     * weeks.
	     */
        public bool isNumberOfNightShiftsLessOrEqualThanThree()
        {
            if (nurse.nightShiftsThisPeriod < 3)
                return true;

            return false;
        }

        /*
         * A nurse must receive at least 2 weekends off duty per 5 week period. A
         * weekend off duty lasts 60 hours including Saturday 00:00 to Monday 04:00.
         */
        public bool isNumberOfFreeWeekendsMoreOrEqualThenTwo()
        {
            int freeWeekends = 4 - nurse.workingWeekends;

            if (freeWeekends >= 2)
                return true;

            return false;
        }

        /*
         * Following a series of at least 2 consecutive night shifts a 42 hours rest
         * is required.
         */
        public bool enoughRestAfterConsecutiveNightShifts()
        {
            int consecutiveNightShifts = nurse.consecutiveNightShifts;

            if (consecutiveNightShifts >= 2)
            {
                int previousShift = getPreviousShift();
                int daysBetween = NurseCalculations.convertShiftToDay(shift)
                        - NurseCalculations.convertShiftToDay(previousShift);

                if (previousShift == -1)
                    return true;

                if (daysBetween > 2)
                    return true;
                else
                {
                    int rest = NurseCalculations.timeBetweenShifts(previousShift, shift);
                    if (rest >= 42)
                        return true;
                    else
                        return false;

                }
            }

            return true;
        }

        /*
         * During any period of 24 consecutive hours, at least 11 hours of rest is
         * required.
         */
        public bool enoughRestIn24Hours()
        {
            int previousShift = getPreviousShift();
            int daysBetween = NurseCalculations.convertShiftToDay(shift)
                    - NurseCalculations.convertShiftToDay(previousShift);

            // its going to be the first shift - no need to rest
            if (previousShift == -1)
                return true;

            if (daysBetween > 1)
                return true;

            else
            {
                int rest = NurseCalculations.timeBetweenShifts(previousShift, shift);
                if (rest >= 11)
                    return true;
                else
                    return false;
            }
        }

        /*
         * A night shift has to be followed by at least 14 hours rest. An exception
         * is that once in a period of 21 days for 24 consecutive hours, the resting
         * time may be reduced to 8 hours.
         */
        public bool enoughRestAfterNightShift()
        {
            int previousShift = getPreviousShift();

            // its going to be the first shift - no need to rest
            if (previousShift == -1)
                return true;

            if (NurseCalculations.isNightShift(previousShift))
            {
                int restTime = NurseCalculations.timeBetweenShifts(previousShift, shift);
                if (restTime >= 14)
                {
                    return true;
                }
                else
                {
                    if (nurse.exceptionForRestAfterNightShift == false)
                    {
                        nurse.exceptionForRestAfterNightShift = true;
                        return true;
                    }
                    return false;
                }
            }

            else
                return true;
        }

        /*
         * The	number	of	consecutive	night	shifts	is	at	most	3.	
         */

        public bool consecutiveNightShiftsConstraint()
        {
            if (nurse.consecutiveNightShifts < 3)
                return true;
            else
                return false;
        }

        /*
         * The	number	of	consecutive	shifts	(workdays)	is	at	most	6.	
         */

        public bool consecutiveWorkdaysConstraint()
        {
            if (nurse.consecutiveShifts < 6)
                return true;
            else
                return false;
        }

        /*
	     * HELPER FUNCTIONS
	     */
        public bool[] getNurseDaySchedule(int nurseId, int shift)
        {
            bool[] nurseDayShedule = new bool[4];
            int day = NurseCalculations.convertShiftToDay(shift);
            int firstShiftThatDay = NurseCalculations.getFirstShiftFromTheDay(day);

            nurseDayShedule[0] = arr[nurseId,firstShiftThatDay];
            nurseDayShedule[1] = arr[nurseId,firstShiftThatDay + 1];
            nurseDayShedule[2] = arr[nurseId,firstShiftThatDay + 2];
            nurseDayShedule[3] = arr[nurseId,firstShiftThatDay + 3];

            return nurseDayShedule;
        }

        public int getPreviousShift()
        {
            bool[] nurseSchedule = this.getRowForNurse(nurseId);
            
            for (int i = shift - 1; i >= 0; i--)
            {
                if (nurseSchedule[i])
                    return i;
            }

            return -1;
        }

        private bool[] getRowForNurse(int nurseId)
        {
            bool[] row = new bool[Constants.SHIFTS_NUMBER];
            for (int i = 0; i < Constants.SHIFTS_NUMBER; i++)
            {
                row[i] = this.arr[nurseId, i];
            }

            return row;
        }

        public int Failed { get; set; }
    }
}
