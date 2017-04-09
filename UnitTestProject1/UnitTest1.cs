using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GeneticAlgorithmSolutionToNSP;
using System.Diagnostics;

namespace UnitTestProject1
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestMethod1()
        {
            int truee=0, falsee = 0;
            Unit unit = new Unit();

            for (int i = 0; i < 10000; i++)
            {
                if (unit.randBool())
                {
                    truee++;
                }
                else
                {
                    falsee++;
                }
            }

            //Trace.Write(truee);
            //Trace.Write(falsee);
            Console.WriteLine(truee);
            Console.WriteLine(falsee);
        }
    }
}
