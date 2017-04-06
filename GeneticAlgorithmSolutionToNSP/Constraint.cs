using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class Constraint
    {
        public int failed;

        public Constraint()
        {
            this.failed = 0;
        }

        int Failed { get; set; }
    }
}
