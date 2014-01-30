using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;

namespace UnitsConverter
{
    public partial class MainPage : PhoneApplicationPage
    {
        private bool convertValue1;

        // Constructor
        public MainPage()
        {
            InitializeComponent();
        }

        private void Convert_Click(object sender, EventArgs e) // is invoked when the user taps the icon in the app bar on the bottom of the screen
        {
            if (convertValue1)
            {
                textBox2.Text = "";
                try
                {
                    double fahrenheightValue = double.Parse(textBox1.Text);
                    double celsiusValue = (fahrenheightValue - 32) * (5.0 / 9.0); // classic conversion formula
                    textBox2.Text = celsiusValue.ToString("F");
                }
                catch (Exception)
                {
                    textBlock2.Text = "Error";
                }
            }
            else
            {
                textBox1.Text = "";
                try
                {
                    double celsiusValue = double.Parse(textBox2.Text);
                    double fahrenheitValue = (celsiusValue * (9.0 / 5.0)) + 32;
                    textBox1.Text = fahrenheitValue.ToString("F");
                }
                catch (Exception) // if the user uses a decimal type of number, the parse method on the double type will throw an exception
                {
                    textBlock1.Text = "Error";
                }
            }
            this.Focus(); // Makes the keyboard focus on the main mage frame, which will ultimately hide the software keyboard
        }

        private void Value1_GotFocus(object sender, RoutedEventArgs e)
        {
            convertValue1 = true;
            textBox1.Text = "";      // text boxes are empty strings so the user interface looks clear to the user
            textBox2.Text = "";
        }

        private void Value2_GotFocus(object sender, RoutedEventArgs e)
        {
            convertValue1 = false;
            textBox1.Text = "";            
            textBox2.Text = "";
        }

    }
}