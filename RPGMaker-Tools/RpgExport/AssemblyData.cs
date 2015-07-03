using System.Collections.Generic;
using static System.String;

namespace Rpg
{
	/// <summary>
	/// Object that acts as storage container for data while parsing an IL file
	/// </summary>
	internal class AssemblyData
	{
		public string MethodDeclaration { get; set; }

		public string MethodName { get; set; }

		public string ClassDeclaration { get; set; }

		public string MethodBefore { get; set; }

		public string MethodAfter { get; set; }

		public int MethodPosition { get; set; }

		public Stack<string> ClassNames { get; set; }

		public List<string> ExternAssembly { get; set; }

		public AssemblyData()
		{
			MethodDeclaration = Empty;
			MethodName = Empty;
			ClassDeclaration = Empty;
			MethodBefore = Empty;
			MethodAfter = Empty;
			MethodPosition = 0;
			ClassNames = new Stack<string>();
			ExternAssembly = new List<string>();
		}
	}
}
