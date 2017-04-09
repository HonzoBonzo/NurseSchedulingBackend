using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    public class Constraint
    {
        public int failed;
        public Nurse nurse;
        public int nurseId;
        public int shift;

        public Constraint()
        {
            this.failed = 0;
        }

        int Failed { get; set; }
    }
}
