using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using MahApps.Metro.Controls;

namespace RPGMaker_Tools
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : MetroWindow
    {
        public MainWindow()
        {
            InitializeComponent();
            /*var tbi = new TaskbarIcon
            {
                Icon = Properties.Resources.B_Dim,
                ToolTipText = "hello world"
            };*/
        }

        private void btn_Exit_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void btn_Options_Click(object sender, RoutedEventArgs e)
        {
            var flyout = this.Flyouts.Items[0] as Flyout;
            if (flyout != null) flyout.IsOpen = !flyout.IsOpen;
        }
    }
}
