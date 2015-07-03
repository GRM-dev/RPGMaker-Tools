namespace Rpg
{
	partial class MainForm
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.components = new System.ComponentModel.Container();
			this.labelIlasm = new System.Windows.Forms.Label();
			this.labelIldasm = new System.Windows.Forms.Label();
			this.labelAssembly = new System.Windows.Forms.Label();
			this.buttonExport = new System.Windows.Forms.Button();
			this.groupBoxPaths = new System.Windows.Forms.GroupBox();
			this.comboIldasm = new System.Windows.Forms.ComboBox();
			this.comboIlasm = new System.Windows.Forms.ComboBox();
			this.toolTip = new System.Windows.Forms.ToolTip(this.components);
			this.textBoxInput = new System.Windows.Forms.TextBox();
			this.textBoxOutput = new System.Windows.Forms.TextBox();
			this.checkBoxOptimize = new System.Windows.Forms.CheckBox();
			this.consoleBox = new System.Windows.Forms.TextBox();
			this.radioCpu64 = new System.Windows.Forms.RadioButton();
			this.radioCpu32 = new System.Windows.Forms.RadioButton();
			this.radioCpuAny = new System.Windows.Forms.RadioButton();
			this.buttonOutput = new System.Windows.Forms.Button();
			this.buttonInput = new System.Windows.Forms.Button();
			this.labelOuput = new System.Windows.Forms.Label();
			this.groupAssembly = new System.Windows.Forms.GroupBox();
			this.groupArchitecture = new System.Windows.Forms.GroupBox();
			this.groupBoxPaths.SuspendLayout();
			this.groupAssembly.SuspendLayout();
			this.groupArchitecture.SuspendLayout();
			this.SuspendLayout();
			// 
			// labelIlasm
			// 
			this.labelIlasm.AutoSize = true;
			this.labelIlasm.Location = new System.Drawing.Point(7, 16);
			this.labelIlasm.Name = "labelIlasm";
			this.labelIlasm.Size = new System.Drawing.Size(114, 13);
			this.labelIlasm.TabIndex = 0;
			this.labelIlasm.Text = "IL Assembler Location:";
			// 
			// labelIldasm
			// 
			this.labelIldasm.AutoSize = true;
			this.labelIldasm.Location = new System.Drawing.Point(7, 60);
			this.labelIldasm.Name = "labelIldasm";
			this.labelIldasm.Size = new System.Drawing.Size(128, 13);
			this.labelIldasm.TabIndex = 1;
			this.labelIldasm.Text = "IL Disassembler Location:";
			// 
			// labelAssembly
			// 
			this.labelAssembly.AutoSize = true;
			this.labelAssembly.Location = new System.Drawing.Point(6, 16);
			this.labelAssembly.Name = "labelAssembly";
			this.labelAssembly.Size = new System.Drawing.Size(81, 13);
			this.labelAssembly.TabIndex = 8;
			this.labelAssembly.Text = "Input Assembly:";
			// 
			// buttonExport
			// 
			this.buttonExport.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.buttonExport.Location = new System.Drawing.Point(451, 432);
			this.buttonExport.Name = "buttonExport";
			this.buttonExport.Size = new System.Drawing.Size(75, 23);
			this.buttonExport.TabIndex = 7;
			this.buttonExport.Text = "Export";
			this.toolTip.SetToolTip(this.buttonExport, "Recompile assembly with exported methods");
			this.buttonExport.UseVisualStyleBackColor = true;
			this.buttonExport.Click += new System.EventHandler(this.buttonExport_Click);
			// 
			// groupBoxPaths
			// 
			this.groupBoxPaths.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.groupBoxPaths.Controls.Add(this.comboIldasm);
			this.groupBoxPaths.Controls.Add(this.labelIlasm);
			this.groupBoxPaths.Controls.Add(this.comboIlasm);
			this.groupBoxPaths.Controls.Add(this.labelIldasm);
			this.groupBoxPaths.Location = new System.Drawing.Point(15, 12);
			this.groupBoxPaths.Name = "groupBoxPaths";
			this.groupBoxPaths.Size = new System.Drawing.Size(411, 109);
			this.groupBoxPaths.TabIndex = 9;
			this.groupBoxPaths.TabStop = false;
			// 
			// comboIldasm
			// 
			this.comboIldasm.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.comboIldasm.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.comboIldasm.FormattingEnabled = true;
			this.comboIldasm.Location = new System.Drawing.Point(10, 76);
			this.comboIldasm.Name = "comboIldasm";
			this.comboIldasm.Size = new System.Drawing.Size(395, 21);
			this.comboIldasm.TabIndex = 17;
			this.toolTip.SetToolTip(this.comboIldasm, "Select location of \"ildasm.exe\"\r\nRelative to \"C:\\%ProgramFilesx86%\\Microsoft SDKs" +
        "\\Windows\"\r\n\r\n");
			// 
			// comboIlasm
			// 
			this.comboIlasm.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.comboIlasm.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.comboIlasm.FormattingEnabled = true;
			this.comboIlasm.Location = new System.Drawing.Point(10, 32);
			this.comboIlasm.Name = "comboIlasm";
			this.comboIlasm.Size = new System.Drawing.Size(395, 21);
			this.comboIlasm.TabIndex = 16;
			this.toolTip.SetToolTip(this.comboIlasm, "Select location of \"ilasm.exe\"\r\nRelative to \"C:\\Windows\\Microsoft.NET\\Framework\"\r" +
        "\n");
			// 
			// textBoxInput
			// 
			this.textBoxInput.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.textBoxInput.DataBindings.Add(new System.Windows.Forms.Binding("Text", global::Rpg.Properties.Settings.Default, "LastInput", true, System.Windows.Forms.DataSourceUpdateMode.OnPropertyChanged));
			this.textBoxInput.Location = new System.Drawing.Point(10, 32);
			this.textBoxInput.Name = "textBoxInput";
			this.textBoxInput.Size = new System.Drawing.Size(465, 20);
			this.textBoxInput.TabIndex = 0;
			this.textBoxInput.Text = global::Rpg.Properties.Settings.Default.LastInput;
			this.toolTip.SetToolTip(this.textBoxInput, "Full path of the input assembly to recompile");
			// 
			// textBoxOutput
			// 
			this.textBoxOutput.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.textBoxOutput.DataBindings.Add(new System.Windows.Forms.Binding("Text", global::Rpg.Properties.Settings.Default, "LastOutput", true, System.Windows.Forms.DataSourceUpdateMode.OnPropertyChanged));
			this.textBoxOutput.Location = new System.Drawing.Point(10, 76);
			this.textBoxOutput.Name = "textBoxOutput";
			this.textBoxOutput.Size = new System.Drawing.Size(465, 20);
			this.textBoxOutput.TabIndex = 12;
			this.textBoxOutput.Text = global::Rpg.Properties.Settings.Default.LastOutput;
			this.toolTip.SetToolTip(this.textBoxOutput, "Full path of the output assembly.\r\nLeave blank to overwrite existing assembly");
			// 
			// checkBoxOptimize
			// 
			this.checkBoxOptimize.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.checkBoxOptimize.AutoSize = true;
			this.checkBoxOptimize.Checked = global::Rpg.Properties.Settings.Default.Optimize;
			this.checkBoxOptimize.CheckState = System.Windows.Forms.CheckState.Checked;
			this.checkBoxOptimize.DataBindings.Add(new System.Windows.Forms.Binding("Checked", global::Rpg.Properties.Settings.Default, "Optimize", true, System.Windows.Forms.DataSourceUpdateMode.OnPropertyChanged));
			this.checkBoxOptimize.Location = new System.Drawing.Point(432, 104);
			this.checkBoxOptimize.Name = "checkBoxOptimize";
			this.checkBoxOptimize.Size = new System.Drawing.Size(66, 17);
			this.checkBoxOptimize.TabIndex = 2;
			this.checkBoxOptimize.Text = "Optimize";
			this.toolTip.SetToolTip(this.checkBoxOptimize, "Perform optimizations during built");
			this.checkBoxOptimize.UseVisualStyleBackColor = true;
			// 
			// consoleBox
			// 
			this.consoleBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.consoleBox.BackColor = System.Drawing.Color.Black;
			this.consoleBox.Font = new System.Drawing.Font("Courier New", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.consoleBox.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
			this.consoleBox.Location = new System.Drawing.Point(15, 243);
			this.consoleBox.Multiline = true;
			this.consoleBox.Name = "consoleBox";
			this.consoleBox.ReadOnly = true;
			this.consoleBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
			this.consoleBox.Size = new System.Drawing.Size(511, 183);
			this.consoleBox.TabIndex = 16;
			this.toolTip.SetToolTip(this.consoleBox, "Compile log");
			this.consoleBox.WordWrap = false;
			// 
			// radioCpu64
			// 
			this.radioCpu64.AutoSize = true;
			this.radioCpu64.Location = new System.Drawing.Point(6, 61);
			this.radioCpu64.Name = "radioCpu64";
			this.radioCpu64.Size = new System.Drawing.Size(42, 17);
			this.radioCpu64.TabIndex = 2;
			this.radioCpu64.Text = "x64";
			this.toolTip.SetToolTip(this.radioCpu64, "Targets x64 (64-bit) architecture\r\n");
			this.radioCpu64.UseVisualStyleBackColor = true;
			// 
			// radioCpu32
			// 
			this.radioCpu32.AutoSize = true;
			this.radioCpu32.Location = new System.Drawing.Point(6, 40);
			this.radioCpu32.Name = "radioCpu32";
			this.radioCpu32.Size = new System.Drawing.Size(42, 17);
			this.radioCpu32.TabIndex = 1;
			this.radioCpu32.Text = "x86";
			this.toolTip.SetToolTip(this.radioCpu32, "Targets x86 (32-bit) architecture");
			this.radioCpu32.UseVisualStyleBackColor = true;
			// 
			// radioCpuAny
			// 
			this.radioCpuAny.AutoSize = true;
			this.radioCpuAny.Checked = true;
			this.radioCpuAny.Location = new System.Drawing.Point(6, 19);
			this.radioCpuAny.Name = "radioCpuAny";
			this.radioCpuAny.Size = new System.Drawing.Size(68, 17);
			this.radioCpuAny.TabIndex = 0;
			this.radioCpuAny.TabStop = true;
			this.radioCpuAny.Text = "Any CPU";
			this.toolTip.SetToolTip(this.radioCpuAny, "Targets both x86 and x64 architectures");
		    this.radioCpuAny.UseVisualStyleBackColor = true;
			// 
			// buttonOutput
			// 
			this.buttonOutput.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.buttonOutput.Image = global::Rpg.Properties.Resources.Open;
			this.buttonOutput.Location = new System.Drawing.Point(481, 73);
			this.buttonOutput.Name = "buttonOutput";
			this.buttonOutput.Size = new System.Drawing.Size(24, 24);
			this.buttonOutput.TabIndex = 13;
			this.toolTip.SetToolTip(this.buttonOutput, "Select output assembly location");
			this.buttonOutput.UseVisualStyleBackColor = true;
			this.buttonOutput.Click += new System.EventHandler(this.buttonOutput_Click);
			// 
			// buttonInput
			// 
			this.buttonInput.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.buttonInput.Image = global::Rpg.Properties.Resources.Open;
			this.buttonInput.Location = new System.Drawing.Point(481, 29);
			this.buttonInput.Name = "buttonInput";
			this.buttonInput.Size = new System.Drawing.Size(24, 24);
			this.buttonInput.TabIndex = 1;
			this.toolTip.SetToolTip(this.buttonInput, "Browse for input assembly");
			this.buttonInput.UseVisualStyleBackColor = true;
			this.buttonInput.Click += new System.EventHandler(this.buttonInput_Click);
			// 
			// labelOuput
			// 
			this.labelOuput.AutoSize = true;
			this.labelOuput.Location = new System.Drawing.Point(6, 60);
			this.labelOuput.Name = "labelOuput";
			this.labelOuput.Size = new System.Drawing.Size(89, 13);
			this.labelOuput.TabIndex = 14;
			this.labelOuput.Text = "Output Assembly:";
			// 
			// groupAssembly
			// 
			this.groupAssembly.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.groupAssembly.Controls.Add(this.labelAssembly);
			this.groupAssembly.Controls.Add(this.labelOuput);
			this.groupAssembly.Controls.Add(this.textBoxInput);
			this.groupAssembly.Controls.Add(this.buttonOutput);
			this.groupAssembly.Controls.Add(this.buttonInput);
			this.groupAssembly.Controls.Add(this.textBoxOutput);
			this.groupAssembly.Location = new System.Drawing.Point(15, 127);
			this.groupAssembly.Name = "groupAssembly";
			this.groupAssembly.Size = new System.Drawing.Size(511, 110);
			this.groupAssembly.TabIndex = 15;
			this.groupAssembly.TabStop = false;
			// 
			// groupArchitecture
			// 
			this.groupArchitecture.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.groupArchitecture.Controls.Add(this.radioCpu64);
			this.groupArchitecture.Controls.Add(this.radioCpu32);
			this.groupArchitecture.Controls.Add(this.radioCpuAny);
			this.groupArchitecture.Location = new System.Drawing.Point(432, 12);
			this.groupArchitecture.Name = "groupArchitecture";
			this.groupArchitecture.Size = new System.Drawing.Size(94, 86);
			this.groupArchitecture.TabIndex = 17;
			this.groupArchitecture.TabStop = false;
			this.groupArchitecture.Text = "Architecture";
			// 
			// MainForm
			// 
			this.AllowDrop = true;
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(538, 467);
			this.Controls.Add(this.buttonDonate);
			this.Controls.Add(this.linkLicense);
			this.Controls.Add(this.groupArchitecture);
			this.Controls.Add(this.consoleBox);
			this.Controls.Add(this.groupAssembly);
			this.Controls.Add(this.groupBoxPaths);
			this.Controls.Add(this.checkBoxOptimize);
			this.Controls.Add(this.buttonExport);
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.MinimumSize = new System.Drawing.Size(441, 331);
			this.Name = "MainForm";
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
			this.Text = "Rpg.NET Export";
			this.toolTip.SetToolTip(this, "Full path to the DLL file containing \r\nfunctions to export. \r\n Does not support E" +
        "XE assemblies.");
			this.DragDrop += new System.Windows.Forms.DragEventHandler(this.MainForm_DragDrop);
			this.DragEnter += new System.Windows.Forms.DragEventHandler(this.MainForm_DragEnter);
			this.groupBoxPaths.ResumeLayout(false);
			this.groupBoxPaths.PerformLayout();
			this.groupAssembly.ResumeLayout(false);
			this.groupAssembly.PerformLayout();
			this.groupArchitecture.ResumeLayout(false);
			this.groupArchitecture.PerformLayout();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label labelIlasm;
		private System.Windows.Forms.Label labelIldasm;
		private System.Windows.Forms.TextBox textBoxInput;
		private System.Windows.Forms.Button buttonInput;
		private System.Windows.Forms.Label labelAssembly;
		private System.Windows.Forms.Button buttonExport;
		private System.Windows.Forms.CheckBox checkBoxOptimize;
		private System.Windows.Forms.GroupBox groupBoxPaths;
		private System.Windows.Forms.ToolTip toolTip;
		private System.Windows.Forms.Label labelOuput;
		private System.Windows.Forms.Button buttonOutput;
		private System.Windows.Forms.TextBox textBoxOutput;
		private System.Windows.Forms.GroupBox groupAssembly;
		private System.Windows.Forms.ComboBox comboIlasm;
		private System.Windows.Forms.ComboBox comboIldasm;
		private System.Windows.Forms.TextBox consoleBox;
		private System.Windows.Forms.GroupBox groupArchitecture;
		private System.Windows.Forms.RadioButton radioCpu64;
		private System.Windows.Forms.RadioButton radioCpu32;
		private System.Windows.Forms.RadioButton radioCpuAny;
		private System.Windows.Forms.LinkLabel linkLicense;
		private System.Windows.Forms.Button buttonDonate;
	}
}

