using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Windows.Forms;

namespace Rpg
{
    /// <summary>
    /// Main form used for the exporter GUI
    /// </summary>
    public partial class MainForm : Form
    {
        #region Events/Delegates

        private delegate void DataReceivedCallback(object sender, DataReceivedEventArgs e);

        #endregion

        #region Fields

        private List<string> _ilasms;
        private List<string> _ildasms;

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public MainForm()
        {
            InitializeComponent();
            Icon = Icon.ExtractAssociatedIcon(Application.ExecutablePath);
            Exporter.DataReceived += Exporter_DataReceived;
            RefreshIlasms();
            RefreshIldasms();
            switch (Properties.Settings.Default.Architecture)
            {
                case 1:
                    radioCpu32.Checked = true;
                    break;
                case 2:
                    radioCpu64.Checked = true;
                    break;
            }
        }

        #endregion

        #region Form Methods

        private void Exporter_DataReceived(object sender, DataReceivedEventArgs e)
        {
            if (e.Data == null) return;
            if (consoleBox.InvokeRequired)
            {
                var callback = new DataReceivedCallback(Exporter_DataReceived);
                BeginInvoke(callback, new object[] { null, e });
            }
            else
                consoleBox.AppendText(e.Data + "\n");
        }

        private void RefreshIlasms()
        {
            const string root = @"C:\Windows\Microsoft.NET\Framework";
            if (!Directory.Exists(root))
            {
                return;
            }
            _ilasms = new List<string>();
            comboIlasm.BeginUpdate();
            comboIlasm.Items.Clear();
            foreach (var dir in Directory.GetDirectories(root))
            {
                var files = Directory.GetFiles(dir, "ilasm.exe");
                if (files.Length == 0)
                    continue;
                _ilasms.Add(files[0]);
                comboIlasm.Items.Add(Path.GetFileName(dir));
            }
            comboIlasm.EndUpdate();
            var index = _ilasms.IndexOf(Properties.Settings.Default.IlasmPath);
            comboIlasm.SelectedIndex = Math.Max(0, index);
        }

        private void RefreshIldasms()
        {
            var programs = IntPtr.Size == 4 ? @"C:\Program Files" : @"C:\Program Files (x86)";
            var root = Path.Combine(programs, @"Microsoft SDKs\Windows");
            if (!Directory.Exists(root))
            {
                return;
            }
            _ildasms = new List<string>();
            comboIldasm.BeginUpdate();
            comboIldasm.Items.Clear();
            foreach (var file in DirectorySearch(root, "ildasm.exe"))
            {
                _ildasms.Add(file);

                var path = file.Replace(root, "").TrimStart('\\');
                comboIldasm.Items.Add(Path.GetDirectoryName(path));
            }
            comboIldasm.EndUpdate();
            var index = _ildasms.IndexOf(Properties.Settings.Default.IldasmPath);
            comboIldasm.SelectedIndex = Math.Max(0, index);
        }

        #endregion

        #region Static Methods

        private static void DisplayError(string msg)
        {
            MessageBox.Show(msg, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        private static List<string> DirectorySearch(string root, string searchPattern)
        {
            var list = new List<string>();
            list.AddRange(Directory.GetFiles(root, searchPattern));
            foreach (var dir in Directory.GetDirectories(root))
                list.AddRange(DirectorySearch(dir, searchPattern));
            return list;
        }

        #endregion

        #region Button Events

        private void buttonInput_Click(object sender, EventArgs e)
        {
            using (var dlg = new OpenFileDialog())
            {
                dlg.Multiselect = false;
                dlg.Filter = ".NET Assembly|*.exe;*.dll|.NET Executable|*.exe|.NET Library|*.dll";
                if (dlg.ShowDialog() == DialogResult.OK)
                    Properties.Settings.Default.LastInput = dlg.FileName;
            }
        }

        private void buttonOutput_Click(object sender, EventArgs e)
        {
            using (var dlg = new SaveFileDialog())
            {
                dlg.Filter = ".NET Library|*.dll|.NET Executable|*.exe";
                dlg.AddExtension = true;
                dlg.DefaultExt = ".dll";
                if (dlg.ShowDialog() == DialogResult.OK)
                    Properties.Settings.Default.LastOutput = dlg.FileName;
            }
        }

        private void buttonExport_Click(object sender, EventArgs e)
        {
            consoleBox.Clear();
            var ilasm = _ilasms[comboIlasm.SelectedIndex];
            var ildasm = _ildasms[comboIldasm.SelectedIndex];
            Properties.Settings.Default.IlasmPath = ilasm;
            Properties.Settings.Default.IldasmPath = ildasm;
            if (radioCpu32.Checked)
                Properties.Settings.Default.Architecture = 1;
            else if (radioCpu64.Checked)
                Properties.Settings.Default.Architecture = 2;
            else
                Properties.Settings.Default.Architecture = 0;
            Properties.Settings.Default.Save();
            var input = Properties.Settings.Default.LastInput;
            Exporter.Build(input);
        }

        #endregion

        #region Drag/Drop

        private void MainForm_DragEnter(object sender, DragEventArgs e)
        {
            e.Effect = e.Data.GetDataPresent(DataFormats.FileDrop) ?
                DragDropEffects.Move : DragDropEffects.None;
        }

        private void MainForm_DragDrop(object sender, DragEventArgs e)
        {
            if (!e.Data.GetDataPresent(DataFormats.FileDrop)) return;
            var files = (string[])e.Data.GetData(DataFormats.FileDrop);
            foreach (var file in files.Where(file => Path.GetExtension(file) == ".dll"))
            {
                textBoxInput.Text = file;
                break;
            }
        }

        #endregion
    }
}
