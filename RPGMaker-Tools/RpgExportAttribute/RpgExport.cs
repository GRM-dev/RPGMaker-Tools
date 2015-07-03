using System;
using System.Runtime.InteropServices;

namespace Rpg
{
    /// <remarks>
    /// Attribute that marks a function to be exported.
    /// </remarks>
    [AttributeUsage(AttributeTargets.Method)]
    public class RpgExport : Attribute
    {
        /// <summary>
        /// Marks this method as an exported function that can be invoked 
        /// via Ruby's Win32API class.
        /// </summary>
        /// <param name="ExportName">Name the method will be invoked as.</param>
        /// <param name="CallingConvention">Indicates the calling convention of the entry point.</param>
        public RpgExport(string ExportName, CallingConvention CallingConvention) { }

        /// <summary>
        /// Marks this method as an exported function that can be invoked via Ruby's 
        /// Win32API class. Defaults to using Cdecl calling convention.
        /// </summary>
        /// <param name="ExportName">Name the method will be invoked as.</param>
        public RpgExport(string ExportName) : this(ExportName, CallingConvention.Cdecl) { }

        /// <summary>
        /// Marks this method as an exported function that can be invoked via Ruby's 
        /// Win32API class.
        /// </summary>
        public RpgExport() : this(null) { }
    }
}