using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class Nurse
    {

        public int id;
        public int hoursPerWeek;
        public int nightShiftThisWeekend;
        public int nightShiftsThisPeriod;
        public int consecutiveNightShifts;
        public bool workedYesterday;
        public int consecutiveShifts;
        public int totalWorkedTime;
        public int workingWeekends;
        public bool exceptionForRestAfterNightShift;
        public bool startedShiftToday;



        public Nurse(int id, int hoursPerWeek)
        {
            this.id = id;
            this.hoursPerWeek = hoursPerWeek;
        }

        public Nurse copyNurse()
        {
            Nurse copy = new Nurse(id, hoursPerWeek);
            copy.nightShiftThisWeekend = nightShiftThisWeekend;
            copy.consecutiveNightShifts = consecutiveNightShifts;
            copy.workedYesterday = workedYesterday;
            copy.consecutiveShifts = consecutiveShifts;
            copy.totalWorkedTime = totalWorkedTime;
            copy.workingWeekends = workingWeekends;
            copy.exceptionForRestAfterNightShift = exceptionForRestAfterNightShift;
            copy.startedShiftToday = startedShiftToday;
            copy.nightShiftsThisPeriod = nightShiftsThisPeriod;

            return copy;
        }


    }
}
