using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{

    public class NurseManager
    {

        public static Nurse[] allNurses = new Nurse[16];
        public static Nurse[] allNursesBackup;

        public static void initializeNurses()
        {
            for (int i = 0; i < 12; i++)
                allNurses[i] = new Nurse(i, 36);

            allNurses[12] = new Nurse(12, 32);
            allNurses[13] = new Nurse(13, 30);
            allNurses[14] = new Nurse(14, 30);
            allNurses[15] = new Nurse(15, 30);
        }

        public static Nurse getNurse(int index)
        {
            return allNurses[index];

        }

        public static void backupNurses()
        {
            allNursesBackup = new Nurse[16];
            for (int i = 0; i < 12; i++)
                allNursesBackup[i] = allNurses[i].copyNurse();

            allNursesBackup[12] = allNurses[12].copyNurse();
            allNursesBackup[13] = allNurses[13].copyNurse();
            allNursesBackup[14] = allNurses[14].copyNurse();
            allNursesBackup[15] = allNurses[15].copyNurse();
        }


    }
}
