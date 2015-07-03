﻿using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.IO;
using System.Linq;

namespace Rpg
{
	static class Program
	{
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
			if (args.Length > 0)
			{
				var argList = args.Select(arg => arg.ToLower()).ToList();
			    // Parse Arguments
				var filepath = argList[0];
				if (!File.Exists(filepath))
				{
					Console.WriteLine("Cannot find file.");
					return;
				}
				var ext = Path.GetExtension(filepath);
				if (ext != ".dll" && ext != ".exe")
				{
					Console.WriteLine("Incorrect file format.");
					return;
				}
				if (argList.Count > 1)
				{
					var output = argList[1];
					ext = Path.GetExtension(output);
					if (!output.StartsWith("/") && (ext == ".dll" || ext == ".exe"))
						Properties.Settings.Default.LastOutput = output;
					if (argList.Contains("/optimize"))
						Properties.Settings.Default.Optimize = true;
					// Architecture
					if (argList.Contains("/anycpu"))
						Properties.Settings.Default.Architecture = 0;
					else if (argList.Contains("/x86"))
						Properties.Settings.Default.Architecture = 1;
					else if (argList.Contains("/x64"))
						Properties.Settings.Default.Architecture = 0;
				}
				if (!argList.Contains("/gui"))
				{
					Properties.Settings.Default.Save();
					Exporter.Build(filepath);
					return;
				}
			}
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new MainForm());
		}
	}
}